package org.arquillian.extension.recorder.screenshooter.browser.impl;

import org.arquillian.extension.recorder.screenshooter.Screenshooter;
import org.arquillian.extension.recorder.screenshooter.ScreenshooterConfiguration;
import org.arquillian.extension.recorder.screenshooter.browser.configuration.BrowserScreenshooterConfiguration;
import org.arquillian.extension.recorder.screenshooter.event.TakeScreenshot;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ScreenshotTaker {
    
    @Inject
    private Instance<ScreenshooterConfiguration> configuration;
    
    @Inject
    private Instance<ServiceLoader> serviceLoader;
    
    @Inject
    private Instance<Screenshooter> screenshooter;

    public void onTakeScreenshot(@Observes TakeScreenshot event) {
        if(configuration.get().getTakeBeforeTest()) {
            TakeScreenshotBeforeTestInterceptor beforeInterceptor = 
                    new TakeScreenshotBeforeTestInterceptor(event, getTakeAndReportService());
            registerInterceptor(beforeInterceptor);
            ((BrowserScreenshooter) screenshooter.get()).setTakeScreenshotBeforeTestInterceptor(beforeInterceptor);
            
            if(((BrowserScreenshooterConfiguration) configuration.get()).getTakeOnEveryAction()) {
                TakeScreenshotOnEveryActionInterceptor onEveryActionInterceptor = 
                        new TakeScreenshotOnEveryActionInterceptor(event, getTakeAndReportService());
                registerInterceptor(onEveryActionInterceptor);
                ((BrowserScreenshooter) screenshooter.get()).setTakeScreenshoOnEveryActionInterceptor(onEveryActionInterceptor);
            }
        } else {
            getTakeAndReportService().takeScreenshotAndReport(event);
        }
    }
    
    private TakeScreenshotAndReportService getTakeAndReportService() {
        return serviceLoader.get().onlyOne(TakeScreenshotAndReportService.class);
    }
    
    private void registerInterceptor(Interceptor interceptor) {
        ((GrapheneProxyInstance) getWebDriver()).registerInterceptor(interceptor);
    }
    
    private WebDriver getWebDriver() {
        return GrapheneContext.getContextFor(Default.class).getWebDriver();
    }
}
