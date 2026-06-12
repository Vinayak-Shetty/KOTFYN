# ExtentReports Guide

This project uses **ExtentReports Spark** as the main HTML reporting UI.

## What ExtentReports Gives

ExtentReports provides:

- Clean HTML report
- Pass/fail/skip stats
- Testcase-wise execution details
- Step logs
- Failure error and stack trace
- Failure screenshot when available
- Group/category filtering

## Where It Is Configured

Main files:

```text
pom.xml
src/test/java/com/mobile/automation/base/BaseTest.java
src/test/java/com/mobile/automation/listeners/ExtentTestListener.java
src/test/java/com/mobile/automation/reporting/ExtentReportManager.java
src/test/java/com/mobile/automation/reporting/ExtentStepLogger.java
src/test/java/com/mobile/automation/steps/RegistrationSteps.java
run-test-with-report.cmd
```

## Maven Dependency

ExtentReports dependency is added in `pom.xml`:

```xml
<dependency>
    <groupId>com.aventstack</groupId>
    <artifactId>extentreports</artifactId>
    <version>5.1.2</version>
    <scope>test</scope>
</dependency>
```

## TestNG Listener

Extent is connected using TestNG listener in `BaseTest`:

```java
@Listeners(ExtentTestListener.class)
public class BaseTest {
}
```

This listener creates one Extent test entry for each TestNG testcase.

## Report Manager

`ExtentReportManager` is responsible for:

- Creating Extent report instance
- Setting report name/title
- Creating output folder
- Managing current testcase report object
- Flushing report at the end

## Step Logging

`ExtentStepLogger` is used to log business steps:

```java
ExtentStepLogger.info("Tap REGISTER NOW");
ExtentStepLogger.pass("Register Your CRN landing screen is displayed");
```

These logs appear inside the testcase details in the Extent report.

## Where Steps Are Added

Step logs are added in:

```text
src/test/java/com/mobile/automation/steps/RegistrationSteps.java
```

Example:

```java
@Step("Tap REGISTER NOW")
public void openSecureSmsPopup() {
    ExtentStepLogger.info("Tap REGISTER NOW");
    registrationPage.tapRegisterNow();
}
```

The `@Step` annotation is still for Allure, but `ExtentStepLogger` is what logs steps into ExtentReports.

## Failure Screenshot

On failure, `ExtentTestListener` captures screenshot from Appium driver:

```java
driver.getScreenshotAs(OutputType.BASE64)
```

The screenshot is attached directly inside the failed testcase in Extent report.

## How To Run Locally

Run:

```powershell
.\run-test-with-report.cmd RegistrationSmokeTest
```

For a single testcase:

```powershell
.\run-test-with-report.cmd RegistrationSmokeTest#TC_SMOKE_REG_001_verifyRegisterYourCrnLandingScreenLoadsSuccessfully
```

## Report Location

Report is generated under:

```text
target/extent-reports/<test-name>-<timestamp>/index.html
```

Example:

```text
target/extent-reports/RegistrationSmokeTest-20260612-124500/index.html
```

## Jenkins Behavior

In Jenkins, the script runs with:

```text
OPEN_REPORT=false
```

So Jenkins will generate the report but will not open the browser.

Jenkins archives reports from:

```text
target/extent-reports/**/*.html
```

## Important Notes

- ExtentReports does not automatically read Allure `@Step` annotations.
- To show steps in Extent, use `ExtentStepLogger`.
- Failure screenshots work only when the Appium driver is available.
- If test setup fails before driver creation, screenshot may not be available.
- The report is generated during TestNG execution, not after execution like Allure.

