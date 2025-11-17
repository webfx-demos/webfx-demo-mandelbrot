package dev.webfx.demo.mandelbrot;

import dev.webfx.demo.mandelbrot.math.MandelbrotMath;
import dev.webfx.demo.mandelbrot.math.MandelbrotPlace;
import dev.webfx.demo.mandelbrot.math.MandelbrotPlaces;
import dev.webfx.demo.mandelbrot.workerthread.MandelbrotWorkerThreadWorker;
import dev.webfx.lib.tracerframework.PixelComputer;
import dev.webfx.lib.tracerframework.TracerEngine;
import dev.webfx.lib.tracerframework.TracerThumbnail;
import dev.webfx.platform.ast.AST;
import dev.webfx.platform.ast.AstObject;
import dev.webfx.platform.ast.ReadOnlyAstObject;
import dev.webfx.platform.typedarray.ArrayBuffer;
import dev.webfx.platform.typedarray.TypedArrayFactory;
import dev.webfx.platform.typedarray.Uint16Array;
import dev.webfx.platform.worker.workerthread.ApplicationWorkerThreadWorkerBase;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;

/**
 * @author Bruno Salmon
 */
final class MandelbrotPixelComputer implements PixelComputer {

    private int canvasWidth, canvasHeight;
    private int placeIndex = -1, frameIndex, thumbnailFrameIndex = -1;
    private boolean usingWebAssembly = true;
    private long currentFrameIterations, lastFrameIterations;

    private int maxIterations;
    private Color mandelbrotColor;
    private Color[] paletteColors;

    MandelbrotPixelComputer() {
    }

    private MandelbrotPixelComputer(int placeIndex, int thumbnailFrameIndex) {
        this.placeIndex = placeIndex;
        this.thumbnailFrameIndex = thumbnailFrameIndex;
    }

    @Override
    public int getPlacesCount() {
        return MandelbrotPlaces.PLACES.length;
    }

    @Override
    public int getFramesCount(int placeIndex) {
        return MandelbrotPlaces.PLACES[placeIndex].lastFrame;
    }

    @Override
    public TracerThumbnail getPlaceThumbnail(int placeIndex) {
        TracerThumbnail placeThumbnail = new TracerThumbnail();
        placeThumbnail.setThumbnailTracer(new TracerEngine(placeThumbnail.getCanvas(), new MandelbrotPixelComputer(placeIndex, MandelbrotPlaces.PLACES[placeIndex].thumbnailFrame)));
        return placeThumbnail;
    }

    public long getLastFrameIterations() {
        return lastFrameIterations;
    }

    public void setUsingWebAssembly(boolean usingWebAssembly) {
        this.usingWebAssembly = usingWebAssembly;
    }

    public boolean isUsingWebAssembly() {
        return usingWebAssembly;
    }

    @Override
    public void initFrame(int canvasWidth, int canvasHeight, int placeIndex, int frameIndex) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        currentFrameIterations = 0;
        if (thumbnailFrameIndex >= 0) {
            placeIndex = this.placeIndex;
            frameIndex = thumbnailFrameIndex;
        }
        MandelbrotMath.init(canvasWidth, canvasHeight, placeIndex, frameIndex);
        if (placeIndex != this.placeIndex || thumbnailFrameIndex >= 0) {
            this.placeIndex = placeIndex;
            MandelbrotPlace place = MandelbrotPlaces.PLACES[placeIndex];
            maxIterations = place.maxIterations;
            mandelbrotColor = Color.valueOf(place.rgbForMandelbrot);
            Palette palette = new Palette(LinearGradient.valueOf(place.linearGradient), place.hsbInterpolation, true);
            int offset = place.paletteMappingOffset;
            int length = place.paletteMappingLength;
            if (length == 0)
                length = maxIterations;
            paletteColors = palette.computePaletteColors(length, offset);
        }
        this.frameIndex = frameIndex;
    }

    @Override
    public void endFrame() {
        lastFrameIterations = currentFrameIterations;
    }

    @Override
    public Color getPixelResultColor(int x, int y, Object linePixelResultStorage) {
        int count = getPixelCount(x, linePixelResultStorage);

        Color color;
        if (count == maxIterations)
            color = mandelbrotColor;
        else
            color = paletteColors[count % paletteColors.length];

        return color;
    }

    private int getPixelCount(int x, Object linePixelResultStorage) {
        if (linePixelResultStorage instanceof Uint16Array uint16Array)
            return uint16Array.get(x);
        return 0;
    }

    @Override
    public Class<? extends ApplicationWorkerThreadWorkerBase> getWorkerClass() {
        return MandelbrotWorkerThreadWorker.class;
    }

    @Override
    public ReadOnlyAstObject getLineWorkerParameters(int y, int n, boolean firstWorkerCall) {
        AstObject astObject = AST.createObject()
            .set("cy", y)
            .set("n", n);
        if (firstWorkerCall) {
            astObject.set("width", canvasWidth)
                .set("height", canvasHeight)
                .set("wasm", usingWebAssembly)
                .set("placeIndex", placeIndex)
                .set("frameIndex", frameIndex);
        }
        return astObject;
    }

    @Override
    public Object getLinePixelResultStorage(Object workerResult) {
        ArrayBuffer arrayBuffer = TypedArrayFactory.wrapNativeBuffer(workerResult);
        Uint16Array uint16Array = TypedArrayFactory.createUint16Array(arrayBuffer);
        int n = uint16Array.getLength();
        for (int i = 0; i < n; i++) {
            currentFrameIterations += uint16Array.get(i);
        }
        return uint16Array;
    }
}
