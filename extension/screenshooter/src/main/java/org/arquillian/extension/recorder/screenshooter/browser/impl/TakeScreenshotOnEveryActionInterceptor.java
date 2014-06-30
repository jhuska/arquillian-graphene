/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arquillian.extension.recorder.screenshooter.browser.impl;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.arquillian.extension.recorder.screenshooter.event.TakeScreenshot;
import org.jboss.arquillian.graphene.proxy.Interceptor;

import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.openqa.selenium.WebDriver;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * 
 */
public class TakeScreenshotOnEveryActionInterceptor extends AbstractTakeScreenshotInterceptor {

    private int counter = 0;

    private static final List<Method> WHITE_LIST_WEB_DRIVER_METHODS = Arrays.asList(WebDriver.class.getMethods());

    public TakeScreenshotOnEveryActionInterceptor(TakeScreenshot takeScreenshotEvent, TakeScreenshotAndReportService service) {
        super(takeScreenshotEvent, service);
    }

    @Override
    public Object intercept(InvocationContext context) throws Throwable {

        Object result = context.invoke();
        Method interceptedMethod = context.getMethod();

        if (isInterceptedMethodAllowed(interceptedMethod)) {
            takeScreenshotAndReport();
        }
        return result;
    }

    private boolean isInterceptedMethodAllowed(Method interceptedMethod) {
        boolean result = false;
        for (Method whiteListMethod : WHITE_LIST_WEB_DRIVER_METHODS) {
            if (methodsEqual(interceptedMethod, whiteListMethod)) {
                result = true;
            }
        }
        return result;
    }

    private static boolean methodsEqual(Method first, Method second) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        if (!first.getName().equals(second.getName())) {
            return false;
        }
        if (first.getParameterTypes().length != second.getParameterTypes().length) {
            return false;
        }
        for (int i = 0; i < first.getParameterTypes().length; i++) {
            if (!first.getParameterTypes()[i].equals(second.getParameterTypes()[i])) {
                return false;
            }
        }
        return true;
    }

}
