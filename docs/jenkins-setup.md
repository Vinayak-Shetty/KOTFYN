# Jenkins Setup Guide

This project can be run from Jenkins using the included `Jenkinsfile` and the ExtentReports integration.

## Prerequisites

Install and configure these on the Jenkins Windows agent:

- Java 17 or later
- Android SDK with `adb` available on `PATH`
- Node.js
- Appium
- Appium UiAutomator2 driver
- Access to the Android emulator or physical Android device

Verify the tools from a terminal on the Jenkins machine:

```powershell
java -version
adb devices
appium --version
```

## Start Appium

Start Appium before running the Jenkins job:

```powershell
appium --base-path /
```

The default project config expects Appium at:

```text
http://127.0.0.1:4723/
```

## Device Setup

Make sure the emulator or physical device is connected:

```powershell
adb devices
```

The app can be launched using the installed package/activity from `config.properties`:

```properties
app.package=com.snapwork.kotak
app.activity=com.snapwork.kotak.MainActivity
```

## Jenkins Job Setup

1. Create a new Jenkins Pipeline job.
2. Point the job to this Git repository.
3. Configure it to use `Jenkinsfile` from SCM.
4. Run the job with the required parameters.

## Pipeline Parameters

The `Jenkinsfile` supports:

| Parameter | Default | Description |
| --- | --- | --- |
| `TEST_NAME` | `RegistrationSmokeTest` | Test class or single method to run |
| `APPIUM_SERVER_URL` | `http://127.0.0.1:4723/` | Appium server URL reachable from Jenkins |
| `DEVICE_NAME` | `Android Emulator` | Android device name capability |
| `MANUAL_WAIT_SECONDS` | `30` | Wait time for manual Net Banking, OTP, and MPIN actions |

Example single testcase value:

```text
RegistrationSmokeTest#TC_SMOKE_REG_001_verifyRegisterYourCrnLandingScreenLoadsSuccessfully
```

## Local Equivalent Command

The same run command works locally:

```powershell
.\run-test-with-report.cmd RegistrationSmokeTest
```

In Jenkins, the pipeline sets:

```text
OPEN_REPORT=false
```

This prevents the script from trying to open the browser on the Jenkins agent.

## Reports

ExtentReports are generated under:

```text
target/extent-reports/<test-name>-<timestamp>/index.html
```

Jenkins archives:

```text
target/extent-reports/**/*.html
target/surefire-reports/**/*
```

The Extent report includes:

- Pass/fail/skip stats
- Testcase details
- Registration business steps
- Failure error and stack trace
- Failure screenshot when available

## Important Manual Step Note

The current `RegistrationSmokeTest` still contains manual steps:

- Net Banking password entry
- OTP entry
- MPIN creation and Continue tap

Jenkins can run the test, but someone must complete those actions on the connected device within `MANUAL_WAIT_SECONDS`. If no one completes the manual action in time, the test will fail.

## Configuration Overrides

`ConfigReader` supports overriding `config.properties` values from Jenkins using environment variables.

Examples:

| Config key | Environment variable |
| --- | --- |
| `appium.server.url` | `APPIUM_SERVER_URL` |
| `device.name` | `DEVICE_NAME` |
| `registration.manual.wait.seconds` | `REGISTRATION_MANUAL_WAIT_SECONDS` |

System properties also work:

```powershell
.\mvn.cmd test -Dtest=RegistrationSmokeTest -Dappium.server.url=http://127.0.0.1:4723/
```

