package com.mobile.automation.reporting;

public final class ExtentStepLogger {
    private ExtentStepLogger() {
    }

    public static void info(String message) {
        ExtentReportManager.getCurrentTest().info(message);
    }

    public static void pass(String message) {
        ExtentReportManager.getCurrentTest().pass(message);
    }

    public static void warning(String message) {
        ExtentReportManager.getCurrentTest().warning(message);
    }
}
