package com.mobile.automation.steps;

import com.mobile.automation.config.ConfigReader;
import com.mobile.automation.pages.LoginPage;
import com.mobile.automation.reporting.ExtentStepLogger;
import io.qameta.allure.Step;
import org.testng.Assert;

public class LoginSteps {
    private static final String LOGIN_MANUAL_WAIT_SECONDS = "login.manual.wait.seconds";

    private final LoginPage loginPage;
    private final RegistrationSteps registrationSteps;

    public LoginSteps(LoginPage loginPage, RegistrationSteps registrationSteps) {
        this.loginPage = loginPage;
        this.registrationSteps = registrationSteps;
    }

    @Step("Ensure registered CRN is ready for login")
    public void ensureRegisteredCrnReadyForLogin() {
        ExtentStepLogger.info("Ensure registered CRN is ready for login");

        if (loginPage.isRegisterNowDisplayed()) {
            ExtentStepLogger.info("Register Now displayed. Running registration flow before login.");
            registrationSteps.verifyRegistrationLandingScreen();
            registrationSteps.openSecureSmsPopup();
            registrationSteps.verifySecureSmsPopup();
            registrationSteps.sendSecureSms();
            registrationSteps.verifyMobileVerificationStarted();
            registrationSteps.verifySelectCrnScreen();
            registrationSteps.selectConfiguredCrn();
            registrationSteps.verifySelectedCrnCanProceed();
            registrationSteps.continueFromSelectCrn();
            registrationSteps.verifyVerifyCrnScreen();
            registrationSteps.waitForManualNetBankingAndVerifyOtpScreen();
            registrationSteps.waitForManualOtpAndVerifyMpinScreen();
            registrationSteps.waitForManualMpinAndVerifyRegistrationSuccessfulScreen();
            registrationSteps.verifyRegistrationSuccessfulScreen();
            registrationSteps.openSignInScreen();
        }

        verifyLoginLandingScreen();
    }

    @Step("Verify Login landing screen is displayed")
    public void verifyLoginLandingScreen() {
        ExtentStepLogger.info("Verify Login landing screen is displayed");
        Assert.assertTrue(
                loginPage.isLoginLandingDisplayed(ConfigReader.get("registration.crn.mask")),
                "Login landing screen should show CRN dropdown, MPIN button, and Net Banking button"
        );
        ExtentStepLogger.pass("Login landing screen is displayed");
    }

    @Step("Navigate to MPIN Login screen")
    public void navigateToMpinLoginScreen() {
        ExtentStepLogger.info("Navigate to MPIN Login screen");
        loginPage.openMpinLogin();
        loginPage.acceptTermsIfDisplayed();
        Assert.assertTrue(loginPage.isMpinLoginScreenDisplayed(), "MPIN Login screen should be displayed");
        ExtentStepLogger.pass("MPIN Login screen is displayed");
    }

    @Step("Login using MPIN")
    public void loginUsingMpin() {
        ExtentStepLogger.info("Login using MPIN");
        loginPage.enterMpin(ConfigReader.get("registration.mpin"));
        loginPage.continueFromLogin();
        ExtentStepLogger.pass("MPIN login submitted");
    }

    @Step("Wait for manual MPIN entry and login result")
    public void waitForManualMpinLoginResult() {
        waitForManualLoginResult("MPIN");
    }

    @Step("Navigate to Net Banking Login screen")
    public void navigateToNetBankingLoginScreen() {
        ExtentStepLogger.info("Navigate to Net Banking Login screen");
        loginPage.openNetBankingLogin();
        loginPage.acceptTermsIfDisplayed();
        Assert.assertTrue(
                loginPage.isNetBankingLoginScreenDisplayed(),
                "Net Banking Login screen should be displayed"
        );
        ExtentStepLogger.pass("Net Banking Login screen is displayed");
    }

    @Step("Login using Net Banking password")
    public void loginUsingNetBankingPassword() {
        ExtentStepLogger.info("Login using Net Banking password");
        loginPage.enterNetBankingPassword(ConfigReader.get("registration.netbanking.password"));
        loginPage.continueFromLogin();
        ExtentStepLogger.pass("Net Banking login submitted");
    }

    @Step("Wait for manual Net Banking password entry and Dashboard")
    public void waitForManualNetBankingLoginAndDashboard() {
        waitForManualLoginAndDashboard("Net Banking password");
    }

    @Step("Verify first-login biometric prompt if displayed")
    public void verifyOptionalBiometricPrompt() {
        ExtentStepLogger.info("Verify first-login biometric prompt if displayed");
        if (loginPage.isBiometricPromptDisplayed()) {
            ExtentStepLogger.pass("Biometric/Fingerprint prompt is displayed");
            return;
        }
        ExtentStepLogger.info("Biometric/Fingerprint prompt was not displayed for this login state");
    }

    @Step("Skip biometric setup if displayed")
    public void skipOptionalBiometricSetupAndVerifyDashboard() {
        ExtentStepLogger.info("Skip biometric setup if displayed");
        if (loginPage.skipBiometricIfDisplayed()) {
            ExtentStepLogger.pass("Biometric/Fingerprint prompt skipped");
        } else {
            ExtentStepLogger.info("Biometric/Fingerprint prompt was not displayed, continuing to Dashboard check");
        }
        verifyDashboardDisplayed();
    }

    @Step("Verify Dashboard is displayed")
    public void verifyDashboardDisplayed() {
        ExtentStepLogger.info("Verify Dashboard is displayed");
        Assert.assertTrue(
                loginPage.isDashboardDisplayed(),
                "Dashboard should load with account summary or dashboard modules visible"
        );
        ExtentStepLogger.pass("Dashboard is displayed");
    }

    @Step("Verify CMS module is visible on Dashboard")
    public void verifyCmsModuleVisible() {
        ExtentStepLogger.info("Verify CMS module is visible on Dashboard");
        Assert.assertTrue(loginPage.isCmsModuleDisplayed(), "CMS module should be visible on Dashboard");
        ExtentStepLogger.pass("CMS module is visible on Dashboard");
    }

    @Step("Verify E-Tax module is visible on Dashboard")
    public void verifyETaxModuleVisible() {
        ExtentStepLogger.info("Verify E-Tax module is visible on Dashboard");
        Assert.assertTrue(loginPage.isETaxModuleDisplayed(), "E-Tax module should be visible on Dashboard");
        ExtentStepLogger.pass("E-Tax module is visible on Dashboard");
    }

    @Step("Verify Trade module is visible on Dashboard")
    public void verifyTradeModuleVisible() {
        ExtentStepLogger.info("Verify Trade module is visible on Dashboard");
        Assert.assertTrue(loginPage.isTradeModuleDisplayed(), "Trade module should be visible on Dashboard");
        ExtentStepLogger.pass("Trade module is visible on Dashboard");
    }

    @Step("Logout from application")
    public void logoutIfDashboardDisplayed() {
        ExtentStepLogger.info("Logout from application");
        if (!loginPage.isDashboardDisplayed()) {
            ExtentStepLogger.info("Dashboard is not displayed, skipping logout");
            return;
        }
        loginPage.openProfile();
        Assert.assertTrue(loginPage.isProfileDisplayed(), "Profile screen should be displayed before logout");
        loginPage.signOut();
        verifyLoginLandingScreen();
        ExtentStepLogger.pass("User logged out successfully");
    }

    private void waitForManualLoginResult(String credentialName) {
        int waitSeconds = ConfigReader.getInt(LOGIN_MANUAL_WAIT_SECONDS, 90);
        ExtentStepLogger.info("Wait for manual " + credentialName + " entry and Continue action");
        System.out.println("Please enter " + credentialName + " manually and tap CONTINUE within "
                + waitSeconds + " seconds.");

        Assert.assertTrue(
                loginPage.waitForDashboardOrBiometricPrompt(waitSeconds),
                "Dashboard or biometric prompt not displayed within " + waitSeconds
                        + " seconds after manual " + credentialName + " login"
        );

        ExtentStepLogger.pass("Login result displayed after manual " + credentialName + " login");
    }

    private void waitForManualLoginAndDashboard(String credentialName) {
        waitForManualLoginResult(credentialName);
        verifyOptionalBiometricPrompt();
        skipOptionalBiometricSetupAndVerifyDashboard();
        ExtentStepLogger.pass("Dashboard displayed after manual " + credentialName + " login");
    }
}
