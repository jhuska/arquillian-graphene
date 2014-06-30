package org.arquillian.extension.recorder.screenshooter.browser.impl;

import java.io.File;
import org.arquillian.extension.recorder.screenshooter.Screenshooter;
import org.arquillian.extension.recorder.screenshooter.Screenshot;
import org.arquillian.extension.recorder.screenshooter.ScreenshotType;
import org.arquillian.extension.recorder.screenshooter.event.TakeScreenshot;
import org.arquillian.recorder.reporter.event.PropertyReportEvent;
import org.arquillian.recorder.reporter.impl.TakenResourceRegister;
import org.arquillian.recorder.reporter.model.entry.ScreenshotEntry;
import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;

/**
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TakeScreenshotAndReportService {
    
    @Inject
    private Instance<Screenshooter> screenshooter;

    @Inject
    private Event<PropertyReportEvent> propertyReportEvent;

    @Inject
    private Instance<TakenResourceRegister> takenScreenshotsRegister;
    
    public void takeScreenshotAndReport(TakeScreenshot takeScreenshotEvent) {
           ScreenshotType screenshotType = screenshooter.get().getScreenshotType();

            File screenshotTarget = new File(
                    new File(takeScreenshotEvent.getMetaData().getTestClassName(), takeScreenshotEvent.getMetaData().getTestMethodName()),
                    takeScreenshotEvent.getFileName());

            Screenshot screenshot = screenshooter.get().takeScreenshot(screenshotTarget, screenshotType);
            takenScreenshotsRegister.get().addTaken(screenshot);

            takeScreenshotEvent.getMetaData().setHeight(screenshot.getHeight());
            takeScreenshotEvent.getMetaData().setWidth(screenshot.getWidth());
            screenshot.setResourceMetaData(takeScreenshotEvent.getMetaData());

            ScreenshotEntry propertyEntry = new ScreenshotEntry();
            propertyEntry.setPath(screenshot.getResource().getAbsolutePath());
            propertyEntry.setPhase(takeScreenshotEvent.getWhen());
            propertyEntry.setType(screenshot.getResourceType().toString());
            propertyEntry.setSize(Long.toString(screenshot.getResource().length()));
            propertyEntry.setWidth(screenshot.getWidth());
            propertyEntry.setHeight(screenshot.getHeight());

            takenScreenshotsRegister.get().addReported(screenshot);
            propertyReportEvent.fire(new PropertyReportEvent(propertyEntry));
    }
}