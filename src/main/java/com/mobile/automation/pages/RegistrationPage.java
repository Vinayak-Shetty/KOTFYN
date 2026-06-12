package com.mobile.automation.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;

public class RegistrationPage extends BasePage {
    private static final String SHORT_WAIT_KEY = "wait.short.seconds";
    private static final String LONG_WAIT_KEY = "wait.long.seconds";

    private final By registerYourCrnTitle = text("Register your CRN");
    private final By registerNowButton = text("REGISTER NOW");
    private final By addNewCrnButton = text("+ ADD NEW CRN");
    private final By secureSmsTitle = text("Send secure SMS");
    private final By sendSmsButton = text("SEND SMS");
    private final By smsDeliveredToast = text("SMS delivered successfully");
    private final By mobileVerificationTitle = text("Just a moment");
    private final By mobileVerificationMessage = text("We are verifying your mobile number.");
    private final By selectCrnTitle = text("Select CRN");
    private final By continueButton = text("CONTINUE");
    private final By verifyCrnTitle = text("Verify your CRN");
    private final By netBankingPasswordField = AppiumBy.id("reg_nbpass");
    private final By netBankingPasswordFieldByResourceId = AppiumBy.androidUIAutomator(
            "new UiSelector().resourceId(\"reg_nbpass\")"
    );
    private final By netBankingPasswordLabel = AppiumBy.id("mat-form-field-label-1");
    private final By netBankingPasswordText = text("Net Banking password");
    private final By otpTitle = text("Enter OTP");
    private final By otpDigit1Field = AppiumBy.id("reg_otp1");
    private final By otpDigit2Field = AppiumBy.id("reg_otp2");
    private final By otpDigit3Field = AppiumBy.id("reg_otp3");
    private final By otpDigit4Field = AppiumBy.id("reg_otp4");
    private final By otpDigit5Field = AppiumBy.id("reg_otp5");
    private final By otpDigit6Field = AppiumBy.id("reg_otp6");
    private final By resendOtpButton = AppiumBy.accessibilityId("RESEND OTP");
    private final By mpinTitle = text("Enter MPIN");
    private final By mpinField = AppiumBy.id("reg_mpin");
    private final By mpinFieldByResourceId = AppiumBy.androidUIAutomator(
            "new UiSelector().resourceId(\"reg_mpin\")"
    );
    private final By registrationSuccessfulTitle = text("Registration successful");
    private final By signInButton = text("SIGN IN");
    private final By signInUsingTitle = text("SIGN IN USING");
    private final By mpinLoginOption = text("MPIN");
    private final By netBankingLoginOption = text("Net Banking");
    private final By androidAllowButton = AppiumBy.id("com.android.permissioncontroller:id/permission_allow_button");
    private final By androidAllowWhileUsingButton = AppiumBy.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button");
    private final By editText = AppiumBy.className("android.widget.EditText");

    public RegistrationPage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isRegistrationLandingDisplayed() {
        openRegistrationLandingIfAlreadyRegistered();
        return isDisplayed(registerYourCrnTitle) && isDisplayed(registerNowButton);
    }

    public void tapRegisterNow() {
        openRegistrationLandingIfAlreadyRegistered();
        tap(registerNowButton);
    }

    public boolean isSecureSmsPopupDisplayed() {
        return isDisplayed(secureSmsTitle) && isDisplayed(sendSmsButton);
    }

    public void tapSendSms() {
        tap(sendSmsButton);
        allowAndroidPermissionIfShown();
    }

    public boolean isMobileVerificationStarted() {
        waitForAnyVisible(mobileVerificationTitle, smsDeliveredToast, selectCrnTitle, otpTitle);
        Duration shortTimeout = shortTimeout();
        return isDisplayed(mobileVerificationTitle, shortTimeout)
                || isDisplayed(mobileVerificationMessage, shortTimeout)
                || isDisplayed(smsDeliveredToast, shortTimeout)
                || isDisplayed(selectCrnTitle, shortTimeout)
                || isDisplayed(otpTitle, shortTimeout);
    }

    public boolean isSelectCrnDisplayed() {
        return isDisplayed(selectCrnTitle, longTimeout())
                && isDisplayed(textContains("Showing CRNs linked with your mobile number"))
                && isDisplayed(continueButton);
    }

    public void selectCrn(String crnText) {
        if (!crnText.isEmpty()) {
            tap(textContains(crnText));
        }
    }

    public boolean isCrnSelected() {
        return isDisplayed(continueButton);
    }

    public void tapContinue() {
        tap(continueButton);
    }

    public void tapContinueAfterHidingKeyboard() {
        hideKeyboardIfShown();
        tap(continueButton);
    }

    public boolean isVerifyCrnDisplayed() {
        return isDisplayed(verifyCrnTitle, longTimeout())
                && isAnyDisplayed(
                longTimeout(),
                netBankingPasswordField,
                netBankingPasswordFieldByResourceId,
                netBankingPasswordLabel,
                netBankingPasswordText
        );
    }

    public void enterNetBankingPassword(String password) {
        clearAndTypeIntoFirstVisible(password, netBankingPasswordField, netBankingPasswordFieldByResourceId);
    }

    public boolean waitForMpinScreen(int seconds) {
        Duration timeout = Duration.ofSeconds(seconds);
        return isDisplayed(mpinTitle, timeout)
                && isAnyDisplayed(timeout, mpinField, mpinFieldByResourceId, editText);
    }

    public boolean waitForOtpScreen(int seconds) {
        return isDisplayed(otpTitle, Duration.ofSeconds(seconds));
    }

    public boolean isOtpDisplayed() {
        return isDisplayed(otpTitle, longTimeout())
                && isDisplayed(otpDigit1Field)
                && isDisplayed(resendOtpButton, shortTimeout());
    }

    public void enterOtp(String otp) {
        enterDigits(otp);
    }

    public boolean isMpinDisplayed() {
        return isDisplayed(mpinTitle, longTimeout())
                && isAnyDisplayed(longTimeout(), mpinField, mpinFieldByResourceId, editText);
    }

    public void enterMpin(String mpin) {
        clearAndTypeIntoFirstVisible(mpin, mpinField, mpinFieldByResourceId, editText);
    }

    public boolean isRegistrationSuccessfulDisplayed() {
        return isDisplayed(registrationSuccessfulTitle, longTimeout()) && isDisplayed(signInButton);
    }

    public boolean waitForRegistrationSuccessfulScreen(int seconds) {
        Duration timeout = Duration.ofSeconds(seconds);
        return isDisplayed(registrationSuccessfulTitle, timeout) && isDisplayed(signInButton, timeout);
    }

    public void tapSignIn() {
        tap(signInButton);
    }

    public boolean isRegisteredCrnReadyForLogin(String crnText) {
        boolean loginOptionsDisplayed = isDisplayed(signInUsingTitle, longTimeout())
                && isDisplayed(mpinLoginOption)
                && isDisplayed(netBankingLoginOption);

        if (crnText.isEmpty()) {
            return loginOptionsDisplayed;
        }

        return loginOptionsDisplayed && isDisplayed(textContains(crnText));
    }

    private void openRegistrationLandingIfAlreadyRegistered() {
        tapIfDisplayed(addNewCrnButton, shortTimeout());
    }

    private void allowAndroidPermissionIfShown() {
        tapIfDisplayed(androidAllowButton, shortTimeout());
        tapIfDisplayed(androidAllowWhileUsingButton, shortTimeout());
    }

    private Duration shortTimeout() {
        return timeout(SHORT_WAIT_KEY, 3);
    }

    private Duration longTimeout() {
        return timeout(LONG_WAIT_KEY, 60);
    }

    private void enterDigits(String value) {
        By[] otpFields = {
                otpDigit1Field,
                otpDigit2Field,
                otpDigit3Field,
                otpDigit4Field,
                otpDigit5Field,
                otpDigit6Field
        };

        if (value.length() == otpFields.length) {
            for (int index = 0; index < otpFields.length; index++) {
                type(otpFields[index], String.valueOf(value.charAt(index)));
            }
            return;
        }

        List<WebElement> fields = driver.findElements(editText);
        if (fields.size() > 1) {
            for (int index = 0; index < value.length() && index < fields.size(); index++) {
                fields.get(index).sendKeys(String.valueOf(value.charAt(index)));
            }
            return;
        }

        type(editText, value);
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
