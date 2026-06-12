package com.mobile.automation.tests;

import com.mobile.automation.base.BaseTest;
import com.mobile.automation.pages.RegistrationPage;
import com.mobile.automation.steps.RegistrationSteps;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RegistrationSmokeTest extends BaseTest {
    private RegistrationSteps registrationSteps;

    @BeforeClass(alwaysRun = true)
    public void openRegistrationPage() {
        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationSteps = new RegistrationSteps(registrationPage);
    }

    @Test(
            description = "TC_SMOKE_REG_001 - Verify Register Your CRN landing screen loads successfully",
            groups = {"smoke", "registration"}
    )
    public void TC_SMOKE_REG_001_verifyRegisterYourCrnLandingScreenLoadsSuccessfully() {
        registrationSteps.verifyRegistrationLandingScreen();
    }

    @Test(
            description = "TC_SMOKE_REG_002 - Verify clicking Register Now opens Secure SMS popup",
            groups = {"smoke", "registration"},
            dependsOnMethods = "TC_SMOKE_REG_001_verifyRegisterYourCrnLandingScreenLoadsSuccessfully"
    )
    public void TC_SMOKE_REG_002_verifyClickingRegisterNowOpensSecureSmsPopup() {
        registrationSteps.openSecureSmsPopup();
        registrationSteps.verifySecureSmsPopup();
    }

    @Test(
            description = "TC_SMOKE_REG_003 - Verify Send SMS initiates mobile verification process",
            groups = {"smoke", "registration"},
            dependsOnMethods = "TC_SMOKE_REG_002_verifyClickingRegisterNowOpensSecureSmsPopup"
    )
    public void TC_SMOKE_REG_003_verifySendSmsInitiatesMobileVerificationProcess() {
        registrationSteps.sendSecureSms();
        registrationSteps.verifyMobileVerificationStarted();
    }

    @Test(
            description = "TC_SMOKE_REG_004 - Verify linked CRNs are fetched and displayed successfully",
            groups = {"smoke", "registration"},
            dependsOnMethods = "TC_SMOKE_REG_003_verifySendSmsInitiatesMobileVerificationProcess"
    )
    public void TC_SMOKE_REG_004_verifyLinkedCrnsAreFetchedAndDisplayedSuccessfully() {
        registrationSteps.verifySelectCrnScreen();
    }

    @Test(
            description = "TC_SMOKE_REG_005 - Verify user can select a CRN and proceed",
            groups = {"smoke", "registration"},
            dependsOnMethods = "TC_SMOKE_REG_004_verifyLinkedCrnsAreFetchedAndDisplayedSuccessfully"
    )
    public void TC_SMOKE_REG_005_verifyUserCanSelectCrnAndProceed() {
        registrationSteps.selectConfiguredCrn();
        registrationSteps.verifySelectedCrnCanProceed();
        registrationSteps.continueFromSelectCrn();
        registrationSteps.verifyVerifyCrnScreen();
    }

    @Test(
            description = "TC_SMOKE_REG_006 - Verify user can verify selected CRN using valid Net Banking credentials",
            groups = {"smoke", "registration"},
            dependsOnMethods = "TC_SMOKE_REG_005_verifyUserCanSelectCrnAndProceed"
    )
    public void TC_SMOKE_REG_006_verifyUserCanVerifyCrnUsingValidNetBankingCredentials() {
        registrationSteps.waitForManualNetBankingAndVerifyOtpScreen();
    }

@Test(
        description = "TC_SMOKE_REG_007 - Verify valid OTP successfully verifies the user",
        groups = {"smoke", "registration"},
        dependsOnMethods = "TC_SMOKE_REG_006_verifyUserCanVerifyCrnUsingValidNetBankingCredentials"
)
public void TC_SMOKE_REG_007_verifyValidOtpSuccessfullyVerifiesTheUser() {

    registrationSteps.waitForManualOtpAndVerifyMpinScreen();
}

    @Test(
            description = "TC_SMOKE_REG_008 - Verify user can create/register MPIN successfully",
            groups = {"smoke", "registration"},
            dependsOnMethods = "TC_SMOKE_REG_007_verifyValidOtpSuccessfullyVerifiesTheUser"
    )
    public void TC_SMOKE_REG_008_verifyUserCanCreateMpinSuccessfully() {
        registrationSteps.waitForManualMpinAndVerifyRegistrationSuccessfulScreen();
    }

    @Test(
            description = "TC_SMOKE_REG_009 - Verify Registration Successful screen is displayed",
            groups = {"smoke", "registration"},
            dependsOnMethods = "TC_SMOKE_REG_008_verifyUserCanCreateMpinSuccessfully"
    )
    public void TC_SMOKE_REG_009_verifyRegistrationSuccessfulScreenIsDisplayed() {
        registrationSteps.verifyRegistrationSuccessfulScreen();
    }

    @Test(
            description = "TC_SMOKE_REG_010 - Verify registered CRN is available on Sign In screen",
            groups = {"smoke", "registration"},
            dependsOnMethods = "TC_SMOKE_REG_009_verifyRegistrationSuccessfulScreenIsDisplayed"
    )
    public void TC_SMOKE_REG_010_verifyRegisteredCrnIsAvailableOnSignInScreen() {
        registrationSteps.openSignInScreen();
        registrationSteps.verifyRegisteredCrnReadyForLogin();
    }
}
