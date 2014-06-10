/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.arquillian.graphene.ftest.page.interception;


import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.ftest.Resource;
import org.jboss.arquillian.graphene.ftest.Resources;
import org.jboss.arquillian.graphene.page.interception.AjaxHalter;
import org.jboss.arquillian.graphene.page.interception.XHRState;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@RunWith(Arquillian.class)
@RunAsClient
public class TestAjaxHalter {

    @ArquillianResource
    private URL contextRoot;

    @Drone
    private WebDriver browser;

    @FindBy
    private WebElement xhr;

    @FindBy
    WebElement status;

    @Deployment
    public static WebArchive createTestArchive() {
        return Resources.inCurrentPackage().all().buildWar("test.war");
    }

    @Before
    public void loadPage() {
        Resource.inCurrentPackage().find("sample.html").loadPage(browser, contextRoot);
    }

    @Test
    public void testFoo() {
//        System.out.println(AjaxHalter.isEnabled() + " ");
        AjaxHalter.enable();
//        System.out.println(AjaxHalter.isEnabled() + " ");
//        System.out.println(AjaxHalter.isHandleAvailable() + " ");

//        Graphene.guardAjax(xhr).click();
        xhr.click();
//        System.out.println(status.getText());

        AjaxHalter handleBlocking = AjaxHalter.getHandleBlocking();
        System.out.println(status.getText());
//        System.out.println(handleBlocking);
        
        handleBlocking.complete();
        System.out.println(status.getText());
        
//        System.out.println(status.getText());
//
//        halter.continueBefore(XHRState.INTERACTIVE);
//        System.out.println(status.getText());
//
//        halter.continueBefore(XHRState.COMPLETE);
//        System.out.println(status.getText());
//
//        halter.complete();
//        System.out.println(status.getText());

    }
}