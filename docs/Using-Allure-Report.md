# Using Allure Report

This framework is configured to generate Allure results from TestNG execution.

## Run Tests

Start Appium first:

```powershell
appium
```

Run all tests:

```powershell
.\mvn.cmd clean test
```

Run a specific test class:

```powershell
.\mvn.cmd test "-Dtest=YourTestClass"
```

## Generate Report

```powershell
.\mvn.cmd allure:report
```

Allure result files are written to:

```text
target/allure-results
```

The generated HTML report is written to:

```text
target/site/allure-maven
```

## Run And Open Report

Use the helper script from the project root:

```powershell
.\run-test-with-report.cmd
```

The script runs tests, generates the report, starts a local report server, and opens the report in the browser. Keep the terminal open while viewing the report. Press `Ctrl + C` to stop the server.

## Useful Files

| File | Purpose |
| --- | --- |
| `src/main/resources/config.properties` | Appium server, APK path, package/activity, reset settings |
| `src/main/java/com/mobile/automation/pages/BasePage.java` | Shared page-object helpers |
| `src/test/java/com/mobile/automation/base/BaseTest.java` | Driver setup and teardown for tests |
| `src/test/java/com/mobile/automation/listeners/AllureTestListener.java` | Allure screenshot attachment support |
| `run-test-with-report.cmd` | Runs tests and opens Allure report |
