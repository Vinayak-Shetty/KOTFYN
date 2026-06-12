package com.mobile.automation.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.mobile.automation.base.BaseTest;
import com.mobile.automation.reporting.ExtentReportManager;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.OutputType;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentTestListener implements ITestListener {
    @Override
    public void onStart(ITestContext context) {
        ExtentReportManager.getInstance();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentReports extentReports = ExtentReportManager.getInstance();
        ExtentTest extentTest = extentReports.createTest(getDisplayName(result));

        String[] groups = result.getMethod().getGroups();
        if (groups != null && groups.length > 0) {
            extentTest.assignCategory(groups);
        }

        extentTest.info("Class: " + result.getTestClass().getName());
        extentTest.info("Method: " + result.getMethod().getMethodName());
        ExtentReportManager.setCurrentTest(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        getCurrentTest().pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest extentTest = getCurrentTest();
        extentTest.fail(result.getThrowable());
        attachFailureScreenshot(result, extentTest);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest extentTest = getCurrentTest();
        Throwable throwable = result.getThrowable();

        if (throwable == null) {
            extentTest.skip("Test skipped");
        } else {
            extentTest.skip(throwable);
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flush();
        ExtentReportManager.removeCurrentTest();
    }

    private ExtentTest getCurrentTest() {
        return ExtentReportManager.getCurrentTest();
    }

    private String getDisplayName(ITestResult result) {
        String description = result.getMethod().getDescription();
        if (description != null && !description.isBlank()) {
            return description;
        }

        return result.getMethod().getMethodName();
    }

    private void attachFailureScreenshot(ITestResult result, ExtentTest extentTest) {
        Object testInstance = result.getInstance();
        if (!(testInstance instanceof BaseTest baseTest)) {
            return;
        }

        AndroidDriver driver = baseTest.getDriver();
        if (driver == null) {
            return;
        }

        try {
            String screenshot = driver.getScreenshotAs(OutputType.BASE64);
            extentTest.log(
                    Status.FAIL,
                    "Failure screenshot",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot).build()
            );
        } catch (RuntimeException screenshotError) {
            extentTest.warning("Unable to capture failure screenshot: " + screenshotError.getMessage());
        }
    }
}
