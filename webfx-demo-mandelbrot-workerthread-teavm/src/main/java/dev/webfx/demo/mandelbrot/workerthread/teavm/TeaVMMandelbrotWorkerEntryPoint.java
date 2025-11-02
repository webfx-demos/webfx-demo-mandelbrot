package dev.webfx.demo.mandelbrot.workerthread.teavm;


import dev.webfx.demo.mandelbrot.workerthread.MandelbrotWorkerThreadWorker;
import dev.webfx.platform.worker.workerthread.teavm.TeaVmWorkerThreadWorkerAdapter;

/**
 * @author Bruno Salmon
 */
public class TeaVMMandelbrotWorkerEntryPoint {

    public static void main(String[] args) {
        TeaVmWorkerThreadWorkerAdapter.executeApplicationWorkerThreadWorker(new MandelbrotWorkerThreadWorker());
    }

}
