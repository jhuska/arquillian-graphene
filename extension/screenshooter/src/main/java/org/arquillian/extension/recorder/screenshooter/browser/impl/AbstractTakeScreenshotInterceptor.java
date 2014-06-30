package org.arquillian.extension.recorder.screenshooter.browser.impl;

import org.arquillian.extension.recorder.screenshooter.event.TakeScreenshot;
import org.jboss.arquillian.graphene.proxy.Interceptor;

/**
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public abstract class AbstractTakeScreenshotInterceptor implements Interceptor {

    protected TakeScreenshot takeScreenshotEvent;
    protected TakeScreenshotAndReportService service;

    public AbstractTakeScreenshotInterceptor(TakeScreenshot takeScreenshotEvent, TakeScreenshotAndReportService service) {
        this.takeScreenshotEvent = takeScreenshotEvent;
        this.service = service;
    }
    
    protected void takeScreenshotAndReport() {
        service.takeScreenshotAndReport(takeScreenshotEvent);
    }
    
    @Override
    public int getPrecedence() {
        return 100;
    }
}
