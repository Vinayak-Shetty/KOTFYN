package com.mobile.automation.tests;

import com.mobile.automation.base.BaseTest;
import com.mobile.automation.config.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LaunchAppTest extends BaseTest {

    @Test(description = "Launch the configured application")
    public void shouldLaunchConfiguredApplication() {
        String expectedPackage = ConfigReader.get("app.package");
        String expectedActivity = ConfigReader.get("app.activity");
        String actualActivity = driver.currentActivity();

        Assert.assertNotNull(driver, "Driver session should be created");
        Assert.assertEquals(driver.getCurrentPackage(), expectedPackage);
        Assert.assertTrue(
                expectedActivity.equals(actualActivity) || expectedActivity.equals(expectedPackage + actualActivity),
                "Expected activity " + expectedActivity + " but found " + actualActivity
        );
    }
}
