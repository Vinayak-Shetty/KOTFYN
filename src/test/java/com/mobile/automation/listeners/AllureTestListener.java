package com.mobile.automation.listeners;

import com.mobile.automation.base.BaseTest;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

public class AllureTestListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        attachScreenshot(result);
    }

    private void attachScreenshot(ITestResult result) {
        Object testInstance = result.getInstance();
        if (!(testInstance instanceof BaseTest baseTest)) {
            return;
        }

        AndroidDriver driver = baseTest.getDriver();
        if (driver == null) {
            return;
        }

        byte[] screenshot = driver.getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment(
                "Failure screenshot",
                "image/png",
                new ByteArrayInputStream(screenshot),
                ".png"
        );
    }
}
