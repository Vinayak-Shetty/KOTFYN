# Commands

Common setup, validation, execution, and reporting commands for this Appium TestNG framework.

| Command | Use |
| --- | --- |
| `npm -v` | Verify that Node.js/npm is installed. |
| `npm install -g appium` | Install Appium globally. |
| `appium --version` | Verify the installed Appium server version. |
| `appium driver install uiautomator2` | Install the UiAutomator2 driver for Android automation. |
| `appium driver list` | List installed and available Appium drivers. |
| `appium` | Start the Appium server before executing mobile tests. |
| `adb devices` | Verify that an Android emulator or real device is connected. |
| `adb shell dumpsys window \| findstr mCurrentFocus` | Find the current Android app package and activity. |
| `.\mvn.cmd clean test` | Clean the Maven project and run tests. |
| `.\mvn.cmd test "-Dtest=YourTestClass"` | Run a specific TestNG test class. |
| `.\mvn.cmd allure:report` | Generate the Allure HTML report from `target/allure-results`. |
| `.\mvn.cmd test allure:report "-Dtest=YourTestClass"` | Run one test class and generate the Allure report. |
| `.\mvn.cmd dependency:tree -Dincludes=org.seleniumhq.selenium,io.appium` | Inspect Selenium and Appium dependency versions. |
| `.\run-test-with-report.cmd` | Run tests, generate the Allure report, and open it through a local server. |
| `powershell -NoProfile -ExecutionPolicy Bypass -File ".\serve-allure-report.ps1"` | Serve the generated Allure report locally. |

## Local Workflow

```powershell
appium
.\mvn.cmd clean test
.\mvn.cmd allure:report
.\run-test-with-report.cmd
```

## Jenkins-Friendly Command

```powershell
.\mvn.cmd test allure:report
```


Frequently used command : 

To run single testcase without report : 
.\mvn.cmd test "-Dtest=RegistrationSmokeTest#TC_SMOKE_REG_001_verifyRegisterYourCrnLandingScreenLoadsSuccessfully"

To run single testcase with report ---- : 
.\run-test-with-report.cmd "RegistrationSmokeTest#TC_SMOKE_REG_001_verifyRegisterYourCrnLandingScreenLoadsSuccessfully"

Use Maven commands directly in Jenkins. Avoid `run-test-with-report.cmd` in Jenkins because it opens the report in a browser for local viewing.
