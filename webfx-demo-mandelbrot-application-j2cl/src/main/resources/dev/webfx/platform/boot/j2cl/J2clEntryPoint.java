package dev.webfx.platform.boot.j2cl;

import dev.webfx.platform.boot.ApplicationBooter;
import dev.webfx.platform.boot.spi.ApplicationBooterProvider;
import org.treblereel.j2cl.processors.annotations.GWT3EntryPoint;

import static dev.webfx.platform.service.gwtj2cl.ServiceRegistry.*;

public final class J2clEntryPoint implements ApplicationBooterProvider {

    @GWT3EntryPoint
    public void entryPoint() {
        registerArrayConstructors();
        registerServiceProviders();
        ApplicationBooter.start(this, null);
    }

    public static void registerArrayConstructors() {

    }

    public static void registerServiceProviders() {
        register(dev.webfx.kit.launcher.spi.WebFxKitLauncherProvider.class, dev.webfx.kit.launcher.spi.impl.gwtj2cl.GwtJ2clWebFxKitLauncherProvider::new);
        register(dev.webfx.kit.mapper.spi.WebFxKitMapperProvider.class, dev.webfx.kit.mapper.spi.impl.gwtj2cl.GwtJ2clWebFxKitHtmlMapperProvider::new);
        register(dev.webfx.platform.arch.spi.ArchProvider.class, dev.webfx.platform.arch.spi.impl.gwtj2cl.GwtJ2clArchProvider::new);
        register(dev.webfx.platform.ast.spi.factory.AstFactoryProvider.class, dev.webfx.platform.ast.spi.factory.impl.generic.GenericAstFactoryProvider::new);
        register(dev.webfx.platform.boot.spi.ApplicationModuleBooter.class, dev.webfx.kit.launcher.WebFxKitLauncherModuleBooter::new, dev.webfx.platform.boot.spi.impl.ApplicationJobsInitializer::new, dev.webfx.platform.boot.spi.impl.ApplicationJobsStarter::new, dev.webfx.platform.resource.spi.impl.j2cl.J2clResourceModuleBooter::new);
        register(dev.webfx.platform.console.spi.ConsoleProvider.class, dev.webfx.platform.console.spi.impl.gwtj2cl.GwtJ2clConsoleProvider::new);
        register(dev.webfx.platform.os.spi.OperatingSystemProvider.class, dev.webfx.platform.os.spi.impl.gwtj2cl.GwtJ2clOperatingSystemProvider::new);
        register(dev.webfx.platform.resource.spi.ResourceProvider.class, dev.webfx.platform.resource.spi.impl.j2cl.J2clResourceProvider::new);
        register(dev.webfx.platform.resource.spi.impl.j2cl.J2clResourceBundle.class, dev.webfx.platform.resource.j2cl.J2clEmbedResourcesBundle.ProvidedJ2clResourceBundle::new);
        register(dev.webfx.platform.scheduler.spi.SchedulerProvider.class, dev.webfx.platform.uischeduler.spi.impl.gwtj2cl.GwtJ2clUiSchedulerProvider::new);
        register(dev.webfx.platform.shutdown.spi.ShutdownProvider.class, dev.webfx.platform.shutdown.spi.impl.gwtj2cl.GwtJ2clShutdownProvider::new);
        register(dev.webfx.platform.uischeduler.spi.UiSchedulerProvider.class, dev.webfx.platform.uischeduler.spi.impl.gwtj2cl.GwtJ2clUiSchedulerProvider::new);
        register(dev.webfx.platform.useragent.spi.UserAgentProvider.class, dev.webfx.platform.useragent.spi.impl.gwtj2cl.GwtJ2clUserAgentProvider::new);
        register(dev.webfx.platform.webworker.spi.WorkerServiceProvider.class, dev.webfx.platform.webworker.spi.impl.gwtj2cl.GwtJ2clWorkerServiceProvider::new);
        register(javafx.application.Application.class, dev.webfx.demo.mandelbrot.MandelbrotApplication::new);
    }
}