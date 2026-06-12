package com.mobile.automation.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

import java.time.Duration;

public class LoginPage extends BasePage {
    private static final String SHORT_WAIT_KEY = "wait.short.seconds";
    private static final String LOGIN_WAIT_KEY = "wait.login.seconds";
    private static final String DASHBOARD_WAIT_KEY = "wait.dashboard.seconds";

    private final By registerNowButton = text("REGISTER NOW");
    private final By crnByMaskedText = text("*****9951");
    private final By crnDropdown = AppiumBy.androidUIAutomator(
            "new UiSelector().className(\"android.widget.TextView\").textContains(\"****\")"
    );
    private final By signInUsingTitle = text("SIGN IN USING");
    private final By mpinLoginOption = text("MPIN");
    private final By netBankingLoginOption = text("Net Banking");
    private final By addNewCrnButton = AppiumBy.accessibilityId("ADD NEW CRN");
    private final By addNewCrnButtonByText = text("+ ADD NEW CRN");
    private final By forgotMpinLink = text("FORGOT MPIN?");

    private final By termsTitle = text("Terms & Condition");
    private final By termsCheckbox = AppiumBy.id("mat-checkbox-1");
    private final By termsCheckboxByResourceId = AppiumBy.androidUIAutomator(
            "new UiSelector().resourceId(\"mat-checkbox-1\")"
    );
    private final By termsCheckboxByText = textContains("I have read the Terms");
    private final By continueButton = AppiumBy.androidUIAutomator(
            "new UiSelector().className(\"android.widget.Button\").text(\"CONTINUE\")"
    );
    private final By continueButtonByText = text("CONTINUE");

    private final By mpinLoginTitle = text("Sign in using MPIN");
    private final By mpinField = AppiumBy.id("mpinlog");
    private final By mpinFieldByResourceId = AppiumBy.androidUIAutomator(
            "new UiSelector().resourceId(\"mpinlog\")"
    );
    private final By mpinFieldByText = textContains("MPIN");
    private final By netBankingLoginTitle = text("Sign in using Net Banking");
    private final By netBankingPasswordField = AppiumBy.id("login_nb");
    private final By netBankingPasswordFieldByResourceId = AppiumBy.androidUIAutomator(
            "new UiSelector().resourceId(\"login_nb\")"
    );
    private final By netBankingPasswordFieldByText = textContains("Net Banking password");
    private final By editText = AppiumBy.className("android.widget.EditText");

    private final By biometricTitle = text("Enable fingerprint login");
    private final By skipForNowButton = text("SKIP FOR NOW");

    private final By overviewTab = text("Overview");
    private final By accountsTab = text("Accounts");
    private final By authorizationsTitle = text("Authorizations");
    private final By cmsTile = text("CMS");
    private final By eTaxTile = text("E-Tax");
    private final By tradeTile = text("Trade");
    private final By savingsAccountText = textContains("Savings");

    private final By profileIconByAccessibility = AppiumBy.accessibilityId("VK");
    private final By profileInitials = text("VK");
    private final By profileTitle = text("Profile");
    private final By profileSignOutButton = AppiumBy.androidUIAutomator(
            "new UiSelector().className(\"android.widget.Button\").text(\"SIGN OUT\")"
    );
    private final By signOutButton = text("SIGN OUT");
    private final By signOutDialogTitle = text("Sign out");
    private final By signOutDialogButton = AppiumBy.xpath("(//android.widget.Button[@text=\"SIGN OUT\"])[2]");
    private final By signOutDialogButtonByText = AppiumBy.androidUIAutomator(
            "new UiSelector().className(\"android.widget.Button\").text(\"SIGN OUT\")"
    );

    public LoginPage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isRegisterNowDisplayed() {
        return isDisplayed(registerNowButton, shortTimeout());
    }

    public boolean isLoginLandingDisplayed(String crnMask) {
        boolean coreLoginControlsDisplayed = isDisplayed(signInUsingTitle, loginTimeout())
                && isDisplayed(mpinLoginOption)
                && isDisplayed(netBankingLoginOption);

        if (!coreLoginControlsDisplayed) {
            return false;
        }

        if (crnMask == null || crnMask.isBlank()) {
            return isAnyDisplayed(shortTimeout(), crnByMaskedText, crnDropdown, addNewCrnButton, addNewCrnButtonByText,
                    forgotMpinLink);
        }

        return isDisplayed(textContains(crnMask), shortTimeout())
                || isAnyDisplayed(shortTimeout(), crnByMaskedText, crnDropdown, addNewCrnButton, addNewCrnButtonByText,
                forgotMpinLink);
    }

    public void selectCrn(String crnMask) {
        if (crnMask != null && !crnMask.isBlank()) {
            tapIfDisplayed(textContains(crnMask), shortTimeout());
            return;
        }
        tapIfDisplayed(crnDropdown, shortTimeout());
    }

    public void openMpinLogin() {
        tap(mpinLoginOption);
    }

    public void openNetBankingLogin() {
        tap(netBankingLoginOption);
    }

    public boolean acceptTermsIfDisplayed() {
        if (!isDisplayed(termsTitle, shortTimeout())) {
            return false;
        }

        tapFirstIfDisplayed(shortTimeout(), termsCheckbox, termsCheckboxByResourceId, termsCheckboxByText);
        tapFirstVisible(continueButton, continueButtonByText);
        return true;
    }

    public boolean isMpinLoginScreenDisplayed() {
        return isDisplayed(mpinLoginTitle, loginTimeout())
                && isAnyDisplayed(loginTimeout(), mpinField, mpinFieldByResourceId, mpinFieldByText, editText);
    }

    public boolean isNetBankingLoginScreenDisplayed() {
        return isDisplayed(netBankingLoginTitle, loginTimeout())
                && isAnyDisplayed(
                loginTimeout(),
                netBankingPasswordField,
                netBankingPasswordFieldByResourceId,
                netBankingPasswordFieldByText,
                editText
        );
    }

    public void enterMpin(String mpin) {
        clearAndTypeIntoFirstVisible(mpin, mpinField, mpinFieldByResourceId, editText);
    }

    public void enterNetBankingPassword(String password) {
        clearAndTypeIntoFirstVisible(password, netBankingPasswordField, netBankingPasswordFieldByResourceId, editText);
    }

    public void continueFromLogin() {
        hideKeyboardIfShown();
        tapFirstVisible(continueButton, continueButtonByText);
    }

    public boolean isBiometricPromptDisplayed() {
        return isDisplayed(biometricTitle, shortTimeout());
    }

    public boolean skipBiometricIfDisplayed() {
        if (!isBiometricPromptDisplayed()) {
            return false;
        }
        tap(skipForNowButton);
        return true;
    }

    public boolean isDashboardDisplayed() {
        return isAnyDisplayed(dashboardTimeout(), overviewTab, accountsTab, authorizationsTitle, savingsAccountText)
                && isAnyDisplayed(loginTimeout(), cmsTile, eTaxTile, tradeTile, savingsAccountText);
    }

    public boolean waitForDashboardOrBiometricPrompt(int seconds) {
        return isAnyDisplayed(
                Duration.ofSeconds(seconds),
                biometricTitle,
                overviewTab,
                accountsTab,
                authorizationsTitle,
                savingsAccountText
        );
    }

    public boolean isCmsModuleDisplayed() {
        return isDisplayed(cmsTile, loginTimeout());
    }

    public boolean isETaxModuleDisplayed() {
        return isDisplayed(eTaxTile, loginTimeout());
    }

    public boolean isTradeModuleDisplayed() {
        return isDisplayed(tradeTile, loginTimeout());
    }

    public void openProfile() {
        tapFirstVisible(profileIconByAccessibility, profileInitials);
    }

    public boolean isProfileDisplayed() {
        return isDisplayed(profileTitle, loginTimeout())
                && isAnyDisplayed(shortTimeout(), profileSignOutButton, signOutButton);
    }

    public void signOut() {
        tapFirstVisible(profileSignOutButton, signOutButton);
        if (isDisplayed(signOutDialogTitle, shortTimeout())) {
            tapFirstIfDisplayed(shortTimeout(), signOutDialogButton, signOutDialogButtonByText, signOutButton);
        }
    }

    private Duration shortTimeout() {
        return timeout(SHORT_WAIT_KEY, 3);
    }

    private Duration loginTimeout() {
        return timeout(LOGIN_WAIT_KEY, 30);
    }

    private Duration dashboardTimeout() {
        return timeout(DASHBOARD_WAIT_KEY, 60);
    }

    private static By text(String value) {
        return AppiumBy.androidUIAutomator("new UiSelector().text(\"" + escape(value) + "\")");
    }

    private static By textContains(String value) {
        return AppiumBy.androidUIAutomator("new UiSelector().textContains(\"" + escape(value) + "\")");
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
