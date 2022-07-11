// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.demo.mandelbrot.application {

    // Direct dependencies modules
    requires java.base;
    requires javafx.graphics;
    requires webfx.demo.mandelbrot.math;
    requires webfx.demo.mandelbrot.webworker;
    requires webfx.lib.tracerframework;
    requires webfx.stack.platform.json;
    requires webfx.stack.platform.webworker;

    // Exported packages
    exports dev.webfx.demo.mandelbrot;

    // Provided services
    provides javafx.application.Application with dev.webfx.demo.mandelbrot.MandelbrotApplication;

}