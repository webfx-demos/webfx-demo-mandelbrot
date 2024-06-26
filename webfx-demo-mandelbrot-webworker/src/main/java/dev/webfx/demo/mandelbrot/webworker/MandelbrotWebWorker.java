package dev.webfx.demo.mandelbrot.webworker;

import dev.webfx.demo.mandelbrot.math.MandelbrotMath;
import dev.webfx.platform.webassembly.WebAssembly;
import dev.webfx.platform.webassembly.WebAssemblyInstance;
import dev.webfx.platform.webassembly.WebAssemblyMemoryBufferReader;
import dev.webfx.platform.ast.AST;
import dev.webfx.platform.ast.ReadOnlyAstObject;
import dev.webfx.platform.webworker.spi.base.JavaCodedWebWorkerBase;
import dev.webfx.platform.async.Future;

/**
 * @author Bruno Salmon
 */
public class MandelbrotWebWorker extends JavaCodedWebWorkerBase {

    private int width, height, placeIndex, frameIndex;
    private int[] pixelIterations; // Output fields that will hold the result of the computation
    // Fields for WebAssembly management
    private boolean usingWebAssembly;
    private WebAssemblyInstance webAssemblyInstance;
    private WebAssemblyMemoryBufferReader outputBufferReader;
    private boolean webAssemblyInstanceInited;

    @Override
    public void onLoaded() {
        if (WebAssembly.isSupported()) {
            Future<WebAssemblyInstance> future = WebAssembly.loadAndInstantiate("classes.wasm"/*, new WebAssemblyImport("mandelbrot", "setPixelIteration", this::setPixelIteration)*/);
            future.onComplete(ar -> {
                webAssemblyInstance = ar.result();
                outputBufferReader = webAssemblyInstance.getDataReader(0);
            });
        }
        setOnMessageHandler(data -> {
            dev.webfx.platform.console.Console.log("Received data: ");
            dev.webfx.platform.console.Console.logNative(data);
            ReadOnlyAstObject json = AST.createObject(data);
            int cy = json.getInteger("cy", 0); // TODO: fix bug returning null for 0 value in TeaVM implementation
            Integer iWidth = json.getInteger("width"); // TODO: fix json.has() not compiling with TeaVM
            boolean initFrame = iWidth != null;
            if (initFrame) {
                width = json.getInteger("width");
                height = json.getInteger("height");
                placeIndex = json.getInteger("placeIndex", 0);
                frameIndex = json.getInteger("frameIndex", 0);
                usingWebAssembly = json.isTrue("wasm");
            }
            if (usingWebAssembly && webAssemblyInstance != null) { // Delegating computation to WebAssembly instance
                if (initFrame || !webAssemblyInstanceInited) {
                    int outputBufferOffset = ((Number) webAssemblyInstance.call("initAndComputeLinePixelIterations", cy, width, height, placeIndex, frameIndex)).intValue();
                    outputBufferReader.setMemoryBufferOffset(outputBufferOffset);
                    webAssemblyInstanceInited = true;
                } else
                    webAssemblyInstance.call("computeLinePixelIterations", cy);
                outputBufferReader.resetMemoryBufferOffset();
                pixelIterations = outputBufferReader.readIntArray(width);
            } else { // Doing computation in this worker
                if (initFrame)
                    MandelbrotMath.init(width, height, placeIndex, frameIndex);
                pixelIterations = MandelbrotMath.createLinePixelResultStorage(width, null);
                MandelbrotMath.computeLinePixelIterations(cy, pixelIterations);
            }
            postMessage(toNativeJsonArray(pixelIterations));
        });
    }

/*
    private void setPixelIteration(int cx, int count) {
        pixelIterations[cx] = count;
    }
*/
}
