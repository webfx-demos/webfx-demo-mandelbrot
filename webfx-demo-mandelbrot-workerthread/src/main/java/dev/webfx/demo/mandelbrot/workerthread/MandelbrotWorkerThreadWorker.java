package dev.webfx.demo.mandelbrot.workerthread;

import dev.webfx.demo.mandelbrot.math.MandelbrotMath;
import dev.webfx.platform.async.Future;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.typedarray.TypedArrayFactory;
import dev.webfx.platform.typedarray.Uint16Array;
import dev.webfx.platform.webassembly.WebAssembly;
import dev.webfx.platform.webassembly.WebAssemblyImport;
import dev.webfx.platform.webassembly.WebAssemblyInstance;
import dev.webfx.platform.webassembly.WebAssemblyMemoryBufferReader;
import dev.webfx.platform.ast.AST;
import dev.webfx.platform.ast.ReadOnlyAstObject;
import dev.webfx.platform.worker.workerthread.ApplicationWorkerThreadWorkerBase;

/**
 * @author Bruno Salmon
 */
public class MandelbrotWorkerThreadWorker extends ApplicationWorkerThreadWorkerBase {

    private int width, height, placeIndex, frameIndex;
    //private int[] pixelIterations; // Output fields that will hold the result of the computation
    private Uint16Array pixelIterationsArray;
    // Fields for WebAssembly management
    private boolean usingWebAssembly;
    private WebAssemblyInstance webAssemblyInstance;
    private WebAssemblyMemoryBufferReader outputBufferReader;
    private boolean webAssemblyInstanceInited;
    //private long t0, t1, t2, t3;

    @Override
    public void onLoaded() {
        /*if (WebAssembly.isSupported()) {
            Future<WebAssemblyInstance> future = WebAssembly.loadAndInstantiate("MandelbrotWorkerThreadWorker.wasm", new WebAssemblyImport("mandelbrot", "setPixelIteration", this::setPixelIteration));
            future.onComplete(ar -> {
                webAssemblyInstance = ar.result();
                outputBufferReader = webAssemblyInstance.getDataReader(0);
            });
        }*/
        setOnMessageHandler((data, transfer) -> {
            /*t0 = System.currentTimeMillis();
            if (t1 > 0)
                Console.log("Messaging time: " + (t0 - t1) + " ms");*/
            ReadOnlyAstObject ast = AST.createObject(data);
            int cy = ast.getInteger("cy", 0);
            int n = ast.getInteger("n", 1);
            Integer iWidth = ast.getInteger("width");
            boolean initFrame = iWidth != null;
            if (initFrame) {
                width = iWidth;
                height = ast.getInteger("height");
                placeIndex = ast.getInteger("placeIndex", 0);
                frameIndex = ast.getInteger("frameIndex", 0);
                usingWebAssembly = ast.isTrue("wasm");
            }
            while (n-- > 0) {
                if (usingWebAssembly && webAssemblyInstance != null) { // Delegating computation to WebAssembly instance
                    if (initFrame || !webAssemblyInstanceInited) {
                        int outputBufferOffset = ((Number) webAssemblyInstance.call("initAndComputeLinePixelIterations", cy, width, height, placeIndex, frameIndex)).intValue();
                        outputBufferReader.setMemoryBufferOffset(outputBufferOffset);
                        webAssemblyInstanceInited = true;
                    } else
                        webAssemblyInstance.call("computeLinePixelIterations", cy);
                    outputBufferReader.resetMemoryBufferOffset();
                    //pixelIterations = outputBufferReader.readIntArray(width);
                } else { // Doing computation in this worker
                    if (initFrame)
                        MandelbrotMath.init(width, height, placeIndex, frameIndex);
                    //pixelIterations = MandelbrotMath.createLinePixelResultStorage(width, null);
                    pixelIterationsArray = TypedArrayFactory.createUint16Array(width);
                    MandelbrotMath.computeLinePixelIterations(cy, pixelIterationsArray);
                }
                Object nativeBuffer = pixelIterationsArray.getTransferableBuffer();
                /*t1 = System.currentTimeMillis();
                Console.log("Computation time: " + (t1 - t0) + " ms");*/
                postMessage(nativeBuffer, nativeBuffer);
                initFrame = false;
                cy++;
            }
        });
    }

    private void setPixelIteration(int cx, int count) {
        pixelIterationsArray.set(cx, count);
        //pixelIterations[cx] = count;
    }

}
