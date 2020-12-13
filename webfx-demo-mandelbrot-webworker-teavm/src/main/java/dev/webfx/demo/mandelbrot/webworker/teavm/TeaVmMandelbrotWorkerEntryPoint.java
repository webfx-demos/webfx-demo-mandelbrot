package dev.webfx.demo.mandelbrot.webworker.teavm;


import dev.webfx.demo.mandelbrot.webworker.MandelbrotWebWorker;
import dev.webfx.platform.teavm.services.webworker.spi.impl.TeaVmRunningWebWorker;

/**
 * @author Bruno Salmon
 */
public class TeaVmMandelbrotWorkerEntryPoint {

    public static void main(String[] args) {
        TeaVmRunningWebWorker.executeJavaCodedWorker(new MandelbrotWebWorker());
    }

}
