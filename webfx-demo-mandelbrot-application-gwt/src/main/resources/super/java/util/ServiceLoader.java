// File managed by WebFX (DO NOT EDIT MANUALLY)
package java.util;

import java.util.Iterator;
import java.util.logging.Logger;
import dev.webfx.platform.shared.util.function.Factory;

public class ServiceLoader<S> implements Iterable<S> {

    public static <S> ServiceLoader<S> load(Class<S> serviceClass) {
        switch (serviceClass.getName()) {
            case "dev.webfx.kit.launcher.spi.WebFxKitLauncherProvider": return new ServiceLoader<S>(dev.webfx.kit.launcher.spi.gwt.GwtWebFxKitLauncherProvider::new);
            case "dev.webfx.kit.mapper.spi.WebFxKitMapperProvider": return new ServiceLoader<S>(dev.webfx.kit.mapper.spi.gwt.GwtWebFxKitHtmlMapperProvider::new);
            case "dev.webfx.platform.client.services.uischeduler.spi.UiSchedulerProvider": return new ServiceLoader<S>(dev.webfx.platform.gwt.services.uischeduler.spi.impl.GwtUiSchedulerProvider::new);
            case "dev.webfx.platform.gwt.services.resource.spi.impl.GwtResourceBundle": return new ServiceLoader<S>();
            case "dev.webfx.platform.shared.services.boot.spi.ApplicationBooterProvider": return new ServiceLoader<S>(dev.webfx.platform.gwt.services.boot.spi.impl.GwtApplicationBooterProvider::new);
            case "dev.webfx.platform.shared.services.boot.spi.ApplicationJob": return new ServiceLoader<S>();
            case "dev.webfx.platform.shared.services.boot.spi.ApplicationModuleBooter": return new ServiceLoader<S>(dev.webfx.kit.launcher.WebFxKitLauncherModuleBooter::new, dev.webfx.platform.gwt.services.resource.spi.impl.GwtResourceModuleBooter::new, dev.webfx.platform.shared.services.boot.spi.impl.ApplicationJobsBooter::new);
            case "dev.webfx.platform.shared.services.log.spi.LoggerProvider": return new ServiceLoader<S>(dev.webfx.platform.gwt.services.log.spi.impl.GwtLoggerProvider::new);
            case "dev.webfx.platform.shared.services.resource.spi.ResourceServiceProvider": return new ServiceLoader<S>(dev.webfx.platform.gwt.services.resource.spi.impl.GwtResourceServiceProvider::new);
            case "dev.webfx.platform.shared.services.scheduler.spi.SchedulerProvider": return new ServiceLoader<S>(dev.webfx.platform.gwt.services.uischeduler.spi.impl.GwtUiSchedulerProvider::new);
            case "dev.webfx.platform.shared.services.shutdown.spi.ShutdownProvider": return new ServiceLoader<S>(dev.webfx.platform.gwt.services.shutdown.spi.impl.GwtShutdownProvider::new);
            case "dev.webfx.stack.platform.json.spi.JsonProvider": return new ServiceLoader<S>(dev.webfx.stack.platform.json.spi.impl.gwt.GwtJsonObject::create);
            case "dev.webfx.stack.platform.webassembly.spi.WebAssemblyProvider": return new ServiceLoader<S>();
            case "dev.webfx.stack.platform.webworker.spi.WorkerServiceProvider": return new ServiceLoader<S>(dev.webfx.stack.platform.webworker.spi.impl.gwt.GwtWorkerServiceProvider::new);
            case "javafx.application.Application": return new ServiceLoader<S>(dev.webfx.demo.mandelbrot.MandelbrotApplication::new);

            // UNKNOWN SPI
            default:
                Logger.getLogger(ServiceLoader.class.getName()).warning("Unknown " + serviceClass + " SPI - returning no provider");
                return new ServiceLoader<S>();
        }
    }

    private final Factory[] factories;

    public ServiceLoader(Factory... factories) {
        this.factories = factories;
    }

    public Iterator<S> iterator() {
        return new Iterator<S>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index < factories.length;
            }

            @Override
            public S next() {
                return (S) factories[index++].create();
            }
        };
    }
}