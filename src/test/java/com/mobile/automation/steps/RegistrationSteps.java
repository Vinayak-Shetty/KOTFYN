package com.mobile.automation.steps;

import com.mobile.automation.config.ConfigReader;
import com.mobile.automation.pages.RegistrationPage;
import com.mobile.automation.reporting.ExtentStepLogger;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.SkipException;

public class RegistrationSteps {
    private static final String MANUAL_WAIT_SECONDS = "registration.manual.wait.seconds";

    private final RegistrationPage registrationPage;

    public RegistrationSteps(RegistrationPage registrationPage) {
        this.registrationPage = registrationPage;
    }

    @Step("Verify Register Your CRN landing screen is displayed")
    public void verifyRegistrationLandingScreen() {
        ExtentStepLogger.info("Verify Register Your CRN landing screen is displayed");
        Assert.assertTrue(
                registrationPage.isRegistrationLandingDisplayed(),
                "Register Your CRN screen should be displayed with Register Now button"
        );
        ExtentStepLogger.pass("Register Your CRN landing screen is displayed");
    }

    @Step("Tap REGISTER NOW")
    public void openSecureSmsPopup() {
        ExtentStepLogger.info("Tap REGISTER NOW");
        registrationPage.tapRegisterNow();
    }

    @Step("Verify Send secure SMS popup is displayed")
    public void verifySecureSmsPopup() {
        ExtentStepLogger.info("Verify Send secure SMS popup is displayed");
        Assert.assertTrue(registrationPage.isSecureSmsPopupDisplayed(), "Send secure SMS popup should be displayed");
        ExtentStepLogger.pass("Send secure SMS popup is displayed");
    }

    @Step("Tap SEND SMS")
    public void sendSecureSms() {
        ExtentStepLogger.info("Tap SEND SMS");
        registrationPage.tapSendSms();
    }

    @Step("Verify mobile verification process starts")
    public void verifyMobileVerificationStarted() {
        ExtentStepLogger.info("Verify mobile verification process starts");
        Assert.assertTrue(
                registrationPage.isMobileVerificationStarted(),
                "Mobile verification should start after tapping Send SMS"
        );
        ExtentStepLogger.pass("Mobile verification process started");
    }

    @Step("Verify Select CRN screen displays linked CRNs")
    public void verifySelectCrnScreen() {
        ExtentStepLogger.info("Verify Select CRN screen displays linked CRNs");
        Assert.assertTrue(registrationPage.isSelectCrnDisplayed(), "Select CRN screen should display linked CRNs");
        ExtentStepLogger.pass("Select CRN screen displays linked CRNs");
    }

    @Step("Select configured CRN")
    public void selectConfiguredCrn() {
        ExtentStepLogger.info("Select configured CRN");
        registrationPage.selectCrn(ConfigReader.get("registration.crn.mask"));
    }

    @Step("Verify selected CRN is ready to proceed")
    public void verifySelectedCrnCanProceed() {
        ExtentStepLogger.info("Verify selected CRN is ready to proceed");
        Assert.assertTrue(registrationPage.isCrnSelected(), "Selected CRN should be ready to proceed");
        ExtentStepLogger.pass("Selected CRN is ready to proceed");
    }

    @Step("Tap CONTINUE on Select CRN screen")
    public void continueFromSelectCrn() {
        ExtentStepLogger.info("Tap CONTINUE on Select CRN screen");
        registrationPage.tapContinue();
    }

    @Step("Verify Verify your CRN screen is displayed")
    public void verifyVerifyCrnScreen() {
        ExtentStepLogger.info("Verify Verify your CRN screen is displayed");
        Assert.assertTrue(
                registrationPage.isVerifyCrnDisplayed(),
                "Verify CRN screen should be displayed after selecting a CRN"
        );
        ExtentStepLogger.pass("Verify your CRN screen is displayed");
    }

    @Step("Enter Net Banking password")
    public void enterNetBankingPassword(String password) {
        ExtentStepLogger.info("Enter Net Banking password");
        registrationPage.enterNetBankingPassword(password);
    }

    @Step("Tap CONTINUE on Verify CRN screen")
    public void continueFromVerifyCrn() {
        ExtentStepLogger.info("Tap CONTINUE on Verify CRN screen");
        registrationPage.tapContinueAfterHidingKeyboard();
    }

    @Step("Verify OTP screen is displayed")
    public void verifyOtpScreen() {
        ExtentStepLogger.info("Verify OTP screen is displayed");
        Assert.assertTrue(
                registrationPage.isOtpDisplayed(),
                "OTP screen should be displayed after valid Net Banking authentication"
        );
        ExtentStepLogger.pass("OTP screen is displayed");
    }

    @Step("Wait for manual Net Banking password and Continue action")
    public void waitForManualNetBankingAndVerifyOtpScreen() {
        int waitSeconds = ConfigReader.getInt(MANUAL_WAIT_SECONDS, 30);
        ExtentStepLogger.info("Wait for manual Net Banking password and Continue action");
        System.out.println("Please enter Net Banking password manually and tap CONTINUE within "
                + waitSeconds + " seconds.");
        Assert.assertTrue(
                registrationPage.waitForOtpScreen(waitSeconds),
                "OTP screen not displayed within " + waitSeconds
                        + " seconds after manual Net Banking verification"
        );
        ExtentStepLogger.pass("OTP screen displayed after manual Net Banking verification");
    }

    @Step("Wait for manual OTP entry and MPIN screen")
    public void waitForManualOtpAndVerifyMpinScreen() {
        int waitSeconds = ConfigReader.getInt(MANUAL_WAIT_SECONDS, 30);
        ExtentStepLogger.info("Wait for manual OTP entry and MPIN screen");
        System.out.println("Please enter OTP manually within " + waitSeconds + " seconds.");
        Assert.assertTrue(
                registrationPage.waitForMpinScreen(waitSeconds),
                "MPIN screen not displayed within " + waitSeconds + " seconds"
        );
        ExtentStepLogger.pass("MPIN screen displayed after manual OTP entry");
    }

    @Step("Wait for manual MPIN entry and Registration successful screen")
    public void waitForManualMpinAndVerifyRegistrationSuccessfulScreen() {
        int waitSeconds = ConfigReader.getInt(MANUAL_WAIT_SECONDS, 30);
        ExtentStepLogger.info("Wait for manual MPIN entry and Registration successful screen");
        System.out.println("Please enter MPIN manually and tap CONTINUE within "
                + waitSeconds + " seconds.");
        Assert.assertTrue(
                registrationPage.waitForRegistrationSuccessfulScreen(waitSeconds),
                "Registration Successful screen not displayed within " + waitSeconds
                        + " seconds after manual MPIN creation"
        );
        ExtentStepLogger.pass("Registration successful screen displayed after manual MPIN creation");
    }

    @Step("Enter MPIN")
    public void enterMpin(String mpin) {
        ExtentStepLogger.info("Enter MPIN");
        registrationPage.enterMpin(mpin);
    }

    @Step("Tap CONTINUE on MPIN screen")
    public void continueFromMpinScreen() {
        ExtentStepLogger.info("Tap CONTINUE on MPIN screen");
        registrationPage.tapContinue();
    }

    @Step("Verify Registration successful screen is displayed")
    public void verifyRegistrationSuccessfulScreen() {
        ExtentStepLogger.info("Verify Registration successful screen is displayed");
        Assert.assertTrue(
                registrationPage.isRegistrationSuccessfulDisplayed(),
                "Registration Successful message and Sign In button should be displayed"
        );
        ExtentStepLogger.pass("Registration successful screen is displayed");
    }

    @Step("Tap SIGN IN")
    public void openSignInScreen() {
        ExtentStepLogger.info("Tap SIGN IN");
        registrationPage.tapSignIn();
    }

    @Step("Verify registered CRN is available with MPIN and Net Banking login options")
    public void verifyRegisteredCrnReadyForLogin() {
        ExtentStepLogger.info("Verify registered CRN is available with MPIN and Net Banking login options");
        Assert.assertTrue(
                registrationPage.isRegisteredCrnReadyForLogin(ConfigReader.get("registration.crn.mask")),
                "Registered CRN should be available with MPIN and Net Banking login options"
        );
        ExtentStepLogger.pass("Registered CRN is available with MPIN and Net Banking login options");
    }

    public String requiredConfig(String key) {
        String value = ConfigReader.get(key);
        if (value.isEmpty()) {
            throw new SkipException("Set " + key + " in src/main/resources/config.properties to run this step");
        }
        return value;
    }
}
