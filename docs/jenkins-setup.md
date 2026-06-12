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

## Start Jenkins Locally

Jenkins runs as a Windows service, so service commands must be run from an Administrator PowerShell window.

To open Administrator PowerShell from a normal PowerShell terminal:

```powershell
Start-Process powershell -Verb RunAs
```

In the new Administrator PowerShell window, start Jenkins:

```powershell
Start-Service Jenkins
Get-Service Jenkins
```

Open Jenkins UI:

```text
http://localhost:8080
```

If Jenkins fails to start and shows service exit code `1067`, stale Jenkins runtime marker files may be present after a crash. Run these from Administrator PowerShell:

```powershell
Stop-Service Jenkins -ErrorAction SilentlyContinue
Move-Item -LiteralPath "C:\ProgramData\Jenkins\jenkins.pid" -Destination "C:\ProgramData\Jenkins\jenkins.pid.stale" -Force
Move-Item -LiteralPath "C:\ProgramData\Jenkins\.jenkins\.owner" -Destination "C:\ProgramData\Jenkins\.jenkins\.owner.stale" -Force
Start-Service Jenkins
Get-Service Jenkins
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
| `TEST_NAME` | `RegistrationSmokeTest,LoginSmokeTest` | Test class, single method, or comma-separated classes to run |
| `APPIUM_SERVER_URL` | `http://127.0.0.1:4723/` | Appium server URL reachable from Jenkins |
| `DEVICE_NAME` | `Android Emulator` | Android device name capability |
| `MANUAL_WAIT_SECONDS` | `30` | Wait time for manual Net Banking, OTP, and MPIN actions |

Example single testcase value:

```text
RegistrationSmokeTest,LoginSmokeTest
```

Example single testcase value:

```text
LoginSmokeTest#TC_SMOKE_LOGIN_001_verifyLoginLandingScreenLoadsSuccessfully
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

In Jenkins, the report is generated under a stable folder:

```text
target/extent-report/index.html
```

Jenkins archives:

```text
target/extent-report/**/*
target/extent-reports/**/*
target/surefire-reports/**/*
```

The Extent report includes:

- Pass/fail/skip stats
- Testcase details
- Registration business steps
- Failure error and stack trace
- Failure screenshot when available

## Viewing Extent Report In Jenkins

Install this Jenkins plugin:

```text
HTML Publisher
```

The pipeline publishes the report as:

```text
Extent Report
```

After a build completes, open the build and click:

```text
Extent Report
```

If the report opens but looks blank or broken, Jenkins may be blocking JavaScript/CSS for archived HTML reports. For local/internal Jenkins only, you can relax the Jenkins Content Security Policy from:

```text
Manage Jenkins > Script Console
```

Run:

```groovy
System.setProperty("hudson.model.DirectoryBrowserSupport.CSP", "")
```

Then refresh the Extent report page.

Note: Disabling CSP is less secure. Use it only on trusted/internal Jenkins instances.

### Permanent CSP Fix

This repository includes a Jenkins startup script:

```text
jenkins/init.groovy.d/disable-html-report-csp.groovy
```

To install it, open PowerShell as Administrator from the project root and run:

```powershell
.\jenkins\install-disable-html-report-csp.ps1
```

Then restart Jenkins:

```powershell
Restart-Service Jenkins
```

The script copies the CSP override to:

```text
%JENKINS_HOME%\init.groovy.d\disable-html-report-csp.groovy
```

After restart, ExtentReports published by Jenkins should load with proper CSS and JavaScript.

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
| `wait.short.seconds` | `WAIT_SHORT_SECONDS` |
| `wait.default.seconds` | `WAIT_DEFAULT_SECONDS` |
| `wait.login.seconds` | `WAIT_LOGIN_SECONDS` |
| `wait.long.seconds` | `WAIT_LONG_SECONDS` |
| `wait.dashboard.seconds` | `WAIT_DASHBOARD_SECONDS` |
| `login.manual.wait.seconds` | `LOGIN_MANUAL_WAIT_SECONDS` |
| `registration.manual.wait.seconds` | `REGISTRATION_MANUAL_WAIT_SECONDS` |

System properties also work:

```powershell
.\mvn.cmd test -Dtest=LoginSmokeTest -Dappium.server.url=http://127.0.0.1:4723/ -Dwait.login.seconds=45
```
