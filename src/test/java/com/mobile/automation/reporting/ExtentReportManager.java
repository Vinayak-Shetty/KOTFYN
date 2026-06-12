package com.mobile.automation.reporting;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.nio.file.Path;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ExtentReportManager {
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
    private static final ThreadLocal<ExtentTest> CURRENT_TEST = new ThreadLocal<>();
    private static ExtentReports extentReports;
    private static String reportPath;

    private ExtentReportManager() {
    }

    public static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            reportPath = resolveReportPath();
            createReportDirectory(reportPath);

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("Kotak Fyn Automation Report");
            sparkReporter.config().setReportName(resolveReportTitle());
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setTimelineEnabled(true);
            sparkReporter.config().setOfflineMode(true);

            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            extentReports.setSystemInfo("Project", "Kotak Corporate FYN");
            extentReports.setSystemInfo("Platform", "Android");
            extentReports.setSystemInfo("Framework", "Appium + TestNG");
        }

        return extentReports;
    }

    public static synchronized void flush() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }

    public static void setCurrentTest(ExtentTest extentTest) {
        CURRENT_TEST.set(extentTest);
    }

    public static ExtentTest getCurrentTest() {
        ExtentTest extentTest = CURRENT_TEST.get();
        if (extentTest == null) {
            extentTest = getInstance().createTest("Untracked test");
            CURRENT_TEST.set(extentTest);
        }

        return extentTest;
    }

    public static void removeCurrentTest() {
        CURRENT_TEST.remove();
    }

    public static String getReportPath() {
        getInstance();
        return reportPath;
    }

    private static String resolveReportPath() {
        String configuredPath = System.getProperty("extent.report.path");
        if (configuredPath != null && !configuredPath.isBlank()) {
            return configuredPath;
        }

        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        return Path.of("target", "extent-reports", "ExtentReport-" + timestamp, "index.html").toString();
    }

    private static String resolveReportTitle() {
        String configuredTitle = System.getProperty("extent.report.title");
        if (configuredTitle != null && !configuredTitle.isBlank()) {
            return configuredTitle;
        }

        return "Kotak Fyn Automation";
    }

    private static void createReportDirectory(String reportPath) {
        try {
            Path parentDirectory = Path.of(reportPath).toAbsolutePath().getParent();
            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }
        } catch (Exception error) {
            throw new IllegalStateException("Unable to create Extent report directory for: " + reportPath, error);
        }
    }
}
