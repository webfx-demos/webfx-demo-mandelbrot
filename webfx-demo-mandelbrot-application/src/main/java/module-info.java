// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.demo.mandelbrot.application {

    // Direct dependencies modules
    requires javafx.graphics;
    requires webfx.demo.mandelbrot.math;
    requires webfx.demo.mandelbrot.workerthread;
    requires webfx.kit.util.scene;
    requires webfx.lib.tracerframework;
    requires webfx.platform.ast;
    requires webfx.platform.console;
    requires webfx.platform.typedarray;
    requires webfx.platform.worker.workerthread;

    // Exported packages
    exports dev.webfx.demo.mandelbrot;

    // Provided services
    provides javafx.application.Application with dev.webfx.demo.mandelbrot.MandelbrotApplication;

}