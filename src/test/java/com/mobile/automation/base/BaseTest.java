package com.mobile.automation.base;

import com.mobile.automation.driver.DriverManager;
import com.mobile.automation.listeners.ExtentTestListener;
import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

@Listeners(ExtentTestListener.class)
public class BaseTest {
    protected AndroidDriver driver;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        driver = DriverManager.getDriver();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }

    public AndroidDriver getDriver() {
        return driver;
    }
}
