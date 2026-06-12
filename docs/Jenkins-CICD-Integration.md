# Jenkins CI/CD Integration for Appium TestNG Allure Framework

## 1. What Is Jenkins?

Jenkins is a CI/CD automation tool. It can automatically run test cases, generate reports, archive results, and show execution status after every build or manual trigger.

## 2. Can Jenkins Be Integrated With This Framework?

Yes. This Appium + TestNG + Maven + Allure framework can be integrated with Jenkins. Jenkins can run the mobile tests and publish the Allure report after execution.

## 3. Jenkins Flow for This Project

1. Pull or access the project code.
2. Start the Appium server.
3. Run TestNG tests using Maven.
4. Generate Allure result files.
5. Publish Allure report in Jenkins.
6. Archive Surefire reports, logs, and screenshots if needed.

## 4. Why Jenkins Is Good for Appium Mobile Automation

Mobile automation needs Android SDK, emulator or real device, Appium, Java, Maven, and the APK. Jenkins can run on the same local or lab machine where all these tools are already configured.

## 5. Required Tools on Jenkins Machine

| Tool | Purpose |
| --- | --- |
| Java JDK 17 or above | Required for Maven and test execution |
| Maven | Build and run TestNG tests |
| Node.js | Required to install/run Appium |
| Appium | Mobile automation server |
| UiAutomator2 Driver | Android automation driver for Appium |
| Android SDK | Required for adb, emulator, and Android device interaction |
| Android Emulator or Real Device | Device on which test cases run |
| APK File | Application under test |

## 6. Recommended Jenkins Plugins

| Plugin | Purpose |
| --- | --- |
| Git Plugin | Pull source code from Git repository |
| Maven Integration Plugin | Run Maven builds easily |
| Allure Jenkins Plugin | Publish Allure reports in Jenkins |
| HTML Publisher Plugin | Optional, to publish generated HTML reports |

## 7. Important Project Paths

| Path | Purpose |
| --- | --- |
| `src/main/resources/config.properties` | Appium server URL, APK path, reset settings |
| `apps/Kotak Corp App Debug.apk` | Current APK file used by the framework |
| `target/allure-results` | Raw Allure result files generated after test execution |
| `target/site/allure-maven` | Generated Allure HTML report |
| `target/surefire-reports` | Maven Surefire/TestNG execution reports |

## 8. Jenkins Build Command

Use this command in a Jenkins Windows batch or PowerShell build step:

```powershell
.\mvn.cmd test allure:report
```

This command runs the available tests and generates the Allure report.

## 9. Starting Appium Server in Jenkins

Before running tests, Appium server must be running. If it is not already running, add a Jenkins build step to start it:

```powershell
Start-Process appium
```

Then run:

```powershell
.\mvn.cmd test allure:report
```

## 10. Configure Allure Report in Jenkins

In Jenkins job configuration, add an Allure report post-build action and set the results path as:

```text
target/allure-results
```

Jenkins will use these result files to render the Allure report inside the build page.

## 11. Recommended Jenkins Job Steps

1. Open Jenkins and create a Freestyle job or Pipeline job.
2. Configure source code location if using Git.
3. Make sure Android device/emulator is available.
4. Start Appium server.
5. Run Maven test command.
6. Publish Allure report from `target/allure-results`.
7. Archive `target/surefire-reports` if needed.

## 12. Suggested Jenkins Pipeline Example

```groovy
pipeline {
    agent any

    stages {
        stage('Start Appium') {
            steps {
                powershell 'Start-Process appium'
            }
        }

        stage('Run Tests') {
            steps {
                powershell '.\\mvn.cmd test allure:report'
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/surefire-reports/**', allowEmptyArchive: true
            allure([
                includeProperties: false,
                jdk: '',
                results: [[path: 'target/allure-results']]
            ])
        }
    }
}
```

## 13. Notes

- Do not use `run-test-with-report.cmd` in Jenkins if it opens a browser. Use Maven commands directly.
- Keep `no.reset=true` and `full.reset=false` if Android blocks app data clearing.
- Make sure the APK path in `config.properties` is valid on the Jenkins machine.
- If Jenkins runs as a Windows service, it may not see interactive emulators. A real device or agent launched from user session is often easier.
