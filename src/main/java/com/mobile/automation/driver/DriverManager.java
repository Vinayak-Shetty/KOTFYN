package com.mobile.automation.driver;

import com.mobile.automation.config.ConfigReader;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public final class DriverManager {
    private static final ThreadLocal<AndroidDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static AndroidDriver getDriver() {
        if (DRIVER.get() == null) {
            DRIVER.set(createAndroidDriver());
        }
        return DRIVER.get();
    }

    public static void quitDriver() {
        AndroidDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (RuntimeException exception) {
                System.err.println("Unable to quit Appium driver cleanly: " + exception.getMessage());
            } finally {
                DRIVER.remove();
            }
        }
    }

    private static AndroidDriver createAndroidDriver() {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(ConfigReader.get("platform.name"))
                .setDeviceName(ConfigReader.get("device.name"))
                .setAutomationName(ConfigReader.get("automation.name"))
                .setNoReset(ConfigReader.getBoolean("no.reset"))
                .setFullReset(ConfigReader.getBoolean("full.reset"))
                .setNewCommandTimeout(Duration.ofSeconds(ConfigReader.getInt("new.command.timeout", 120)));

        setOptionalCapabilities(options);

        try {
            AndroidDriver driver = new AndroidDriver(new URL(ConfigReader.get("appium.server.url")), options);
            activateConfiguredApp(driver);
            return driver;
        } catch (MalformedURLException exception) {
            throw new IllegalStateException("Invalid Appium server URL", exception);
        }
    }

    private static void activateConfiguredApp(AndroidDriver driver) {
        String appPackage = ConfigReader.get("app.package");
        if (appPackage.isEmpty()) {
            Object appPackageCapability = driver.getCapabilities().getCapability("appium:appPackage");
            if (appPackageCapability == null) {
                appPackageCapability = driver.getCapabilities().getCapability("appPackage");
            }
            appPackage = appPackageCapability == null ? "" : appPackageCapability.toString();
        }

        if (!appPackage.isEmpty()) {
            driver.activateApp(appPackage);
        }
    }

    private static void setOptionalCapabilities(UiAutomator2Options options) {
        String platformVersion = ConfigReader.get("platform.version");
        String appPath = ConfigReader.get("app.path");
        String appPackage = ConfigReader.get("app.package");
        String appActivity = ConfigReader.get("app.activity");
        boolean hasAppPath = !appPath.isEmpty() && !appPath.contains("path\\to\\your");
        boolean hasAppPackage = !appPackage.isEmpty();
        boolean hasAppActivity = !appActivity.isEmpty();

        if (!platformVersion.isEmpty()) {
            options.setPlatformVersion(platformVersion);
        }

        if (hasAppPath) {
            if (!Files.exists(Path.of(appPath))) {
                throw new IllegalStateException("Configured app.path does not exist: " + appPath);
            }
            options.setApp(appPath);
            return;
        }

        if (hasAppPackage && hasAppActivity) {
            options.setAppPackage(appPackage);
            options.setAppActivity(appActivity);
            return;
        }

        throw new IllegalStateException(
                "No app under test is configured. Provide either a valid app.path to an APK, "
                        + "or install the app on the device and set both app.package and app.activity "
                        + "in src/main/resources/config.properties."
        );
    }
}
