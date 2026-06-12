# Java Appium POM Framework

Reusable Java, TestNG, Appium, and Allure framework for mobile automation.

## What Is Included

- Appium driver setup through `DriverManager`
- Config loading from `src/main/resources/config.properties`
- Shared page-object helpers in `BasePage`
- Shared test setup/teardown in `BaseTest`
- Allure TestNG listener support
- Maven build and report generation scripts

## Project Structure

```text
pom.xml
testng.xml
run-test-with-report.cmd
src/main/java/com/mobile/automation/
  config/ConfigReader.java
  driver/DriverManager.java
  pages/BasePage.java
src/main/resources/
  config.properties
src/test/java/com/mobile/automation/
  base/BaseTest.java
  listeners/AllureTestListener.java
  tests/
src/test/resources/
  allure.properties
```

## Configure The App

Update `src/main/resources/config.properties`.

Use an APK:

```properties
app.path=C:\\path\\to\\your\\application.apk
```

Or use an already installed app:

```properties
app.package=com.yourcompany.yourapp
app.activity=com.yourcompany.yourapp.MainActivity
```

## Add New Tests

Create page objects under:

```text
src/main/java/com/mobile/automation/pages/
```

Create tests under:

```text
src/test/java/com/mobile/automation/tests/
```

New tests can extend `BaseTest` and use `driver` from the base class.

## Run

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

Run tests and generate the Allure report:

```powershell
.\run-test-with-report.cmd
```

Run one test class, generate Allure, and open the report in the browser after the test passes:

```powershell
.\run-test-with-report.cmd LaunchAppTest
```

Each report is created in a timestamped folder under `target/allure-reports`, for example:

```text
target/allure-reports/LaunchAppTest-20260611-135900/index.html
```

The report opens through a local server so the Allure page can load its JSON files correctly.
