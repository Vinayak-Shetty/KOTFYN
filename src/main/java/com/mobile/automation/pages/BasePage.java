package com.mobile.automation.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    protected final AndroidDriver driver;
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(20);

    protected BasePage(AndroidDriver driver) {
        this.driver = driver;
    }

    protected WebElement waitForVisible(By locator) {
        return waitForVisible(locator, DEFAULT_TIMEOUT);
    }

    protected WebElement waitForVisible(By locator, Duration timeout) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForAnyVisible(By... locators) {
        return new WebDriverWait(driver, DEFAULT_TIMEOUT)
                .until(driver -> {
                    for (By locator : locators) {
                        for (WebElement element : driver.findElements(locator)) {
                            if (element.isDisplayed()) {
                                return element;
                            }
                        }
                    }
                    return null;
                });
    }

    protected boolean isAnyDisplayed(Duration timeout, By... locators) {
        try {
            return new WebDriverWait(driver, timeout)
                    .until(driver -> {
                        for (By locator : locators) {
                            for (WebElement element : driver.findElements(locator)) {
                                if (element.isDisplayed()) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    });
        } catch (RuntimeException exception) {
            return false;
        }
    }

    protected WebElement waitForClickable(By locator) {
        return new WebDriverWait(driver, DEFAULT_TIMEOUT)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waitForVisible(locator).isDisplayed();
        } catch (RuntimeException exception) {
            return false;
        }
    }

    protected boolean isDisplayed(By locator, Duration timeout) {
        try {
            return waitForVisible(locator, timeout).isDisplayed();
        } catch (RuntimeException exception) {
            return false;
        }
    }

    protected void tap(By locator) {
        waitForClickable(locator).click();
    }

    protected boolean tapIfDisplayed(By locator, Duration timeout) {
        if (!isDisplayed(locator, timeout)) {
            return false;
        }
        driver.findElement(locator).click();
        return true;
    }

    protected void type(By locator, String value) {
        WebElement element = waitForVisible(locator);
        element.click();
        element.sendKeys(value);
    }

    protected void typeIntoFirstVisible(String value, By... locators) {
        WebElement element = waitForAnyVisible(locators);
        element.click();
        element.sendKeys(value);
    }

    protected void clearAndTypeIntoFirstVisible(String value, By... locators) {
        WebElement element = waitForAnyVisible(locators);
        element.click();
        try {
            element.clear();
        } catch (RuntimeException ignored) {
            // Some hybrid WebView inputs do not support clear reliably.
        }
        element.sendKeys(value);
    }

    protected void hideKeyboardIfShown() {
        try {
            driver.hideKeyboard();
        } catch (RuntimeException ignored) {
            // Keyboard is already hidden or the device could not dismiss it.
        }
    }

    protected void rotateToLandscape() {
        driver.rotate(ScreenOrientation.LANDSCAPE);
    }

    protected void rotateToPortrait() {
        driver.rotate(ScreenOrientation.PORTRAIT);
    }
}
