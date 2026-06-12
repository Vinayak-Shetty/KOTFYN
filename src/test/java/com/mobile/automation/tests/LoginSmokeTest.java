package com.mobile.automation.tests;

import com.mobile.automation.base.BaseTest;
import com.mobile.automation.pages.LoginPage;
import com.mobile.automation.pages.RegistrationPage;
import com.mobile.automation.steps.LoginSteps;
import com.mobile.automation.steps.RegistrationSteps;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginSmokeTest extends BaseTest {
    private LoginSteps loginSteps;

    @BeforeClass(alwaysRun = true)
    public void openLoginPage() {
        RegistrationPage registrationPage = new RegistrationPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        RegistrationSteps registrationSteps = new RegistrationSteps(registrationPage);
        loginSteps = new LoginSteps(loginPage, registrationSteps);
    }

    @Test(
            description = "TC_SMOKE_LOGIN_001 - Verify Login landing screen loads successfully",
            groups = {"smoke", "login"}
    )
    public void TC_SMOKE_LOGIN_001_verifyLoginLandingScreenLoadsSuccessfully() {
        loginSteps.ensureRegisteredCrnReadyForLogin();
    }

    @Test(
            description = "TC_SMOKE_LOGIN_002 - Verify user can navigate to MPIN Login screen",
            groups = {"smoke", "login"},
            dependsOnMethods = "TC_SMOKE_LOGIN_001_verifyLoginLandingScreenLoadsSuccessfully"
    )
    public void TC_SMOKE_LOGIN_002_verifyUserCanNavigateToMpinLoginScreen() {
        loginSteps.navigateToMpinLoginScreen();
    }

    @Test(
            description = "TC_SMOKE_LOGIN_003 - Verify successful login using valid MPIN",
            groups = {"smoke", "login"},
            dependsOnMethods = "TC_SMOKE_LOGIN_002_verifyUserCanNavigateToMpinLoginScreen"
    )
    public void TC_SMOKE_LOGIN_003_verifySuccessfulLoginUsingValidMpin() {
        loginSteps.waitForManualMpinLoginResult();
    }

    @Test(
            description = "TC_SMOKE_LOGIN_004 - Verify biometric prompt appears after first successful login",
            groups = {"smoke", "login"},
            dependsOnMethods = "TC_SMOKE_LOGIN_003_verifySuccessfulLoginUsingValidMpin"
    )
    public void TC_SMOKE_LOGIN_004_verifyBiometricPromptAfterFirstSuccessfulLogin() {
        loginSteps.verifyOptionalBiometricPrompt();
    }

    @Test(
            description = "TC_SMOKE_LOGIN_005 - Verify user can skip biometric setup",
            groups = {"smoke", "login"},
            dependsOnMethods = "TC_SMOKE_LOGIN_004_verifyBiometricPromptAfterFirstSuccessfulLogin"
    )
    public void TC_SMOKE_LOGIN_005_verifyUserCanSkipBiometricSetup() {
        loginSteps.skipOptionalBiometricSetupAndVerifyDashboard();
    }

    @Test(
            description = "TC_SMOKE_LOGIN_006 - Verify user is able to logout successfully",
            groups = {"smoke", "login"},
            dependsOnMethods = "TC_SMOKE_LOGIN_005_verifyUserCanSkipBiometricSetup"
    )
    public void TC_SMOKE_LOGIN_006_verifyUserCanLogoutSuccessfully() {
        loginSteps.logoutIfDashboardDisplayed();
    }

    @Test(
            description = "TC_SMOKE_LOGIN_007 - Verify user can navigate to Net Banking Login screen",
            groups = {"smoke", "login"},
            dependsOnMethods = "TC_SMOKE_LOGIN_006_verifyUserCanLogoutSuccessfully"
    )
    public void TC_SMOKE_LOGIN_007_verifyUserCanNavigateToNetBankingLoginScreen() {
        loginSteps.navigateToNetBankingLoginScreen();
    }

    @Test(
            description = "TC_SMOKE_LOGIN_008 - Verify successful login using valid Net Banking password",
            groups = {"smoke", "login"},
            dependsOnMethods = "TC_SMOKE_LOGIN_007_verifyUserCanNavigateToNetBankingLoginScreen"
    )
    public void TC_SMOKE_LOGIN_008_verifySuccessfulLoginUsingValidNetBankingPassword() {
        loginSteps.waitForManualNetBankingLoginAndDashboard();
    }

    @Test(
            description = "TC_SMOKE_LOGIN_009 - Verify Dashboard loads successfully after login",
            groups = {"smoke", "login"},
            dependsOnMethods = "TC_SMOKE_LOGIN_008_verifySuccessfulLoginUsingValidNetBankingPassword"
    )
    public void TC_SMOKE_LOGIN_009_verifyDashboardLoadsSuccessfullyAfterLogin() {
        loginSteps.verifyDashboardDisplayed();
    }

    @Test(
            description = "TC_SMOKE_LOGIN_010 - Verify CMS module is visible from Dashboard",
            groups = {"smoke", "login"},
            dependsOnMethods = "TC_SMOKE_LOGIN_009_verifyDashboardLoadsSuccessfullyAfterLogin"
    )
    public void TC_SMOKE_LOGIN_010_verifyCmsModuleIsVisibleFromDashboard() {
        loginSteps.verifyCmsModuleVisible();
    }

    @Test(
            description = "TC_SMOKE_LOGIN_011 - Verify E-Tax module is visible from Dashboard",
            groups = {"smoke", "login"},
            dependsOnMethods = "TC_SMOKE_LOGIN_010_verifyCmsModuleIsVisibleFromDashboard"
    )
    public void TC_SMOKE_LOGIN_011_verifyETaxModuleIsVisibleFromDashboard() {
        loginSteps.verifyETaxModuleVisible();
    }

    @Test(
            description = "TC_SMOKE_LOGIN_012 - Verify Trade module is visible from Dashboard",
            groups = {"smoke", "login"},
            dependsOnMethods = "TC_SMOKE_LOGIN_011_verifyETaxModuleIsVisibleFromDashboard"
    )
    public void TC_SMOKE_LOGIN_012_verifyTradeModuleIsVisibleFromDashboard() {
        loginSteps.verifyTradeModuleVisible();
    }

}
