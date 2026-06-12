# Tool Versions Used

This document lists the important tools, libraries, and versions used in this automation project so far.

## Local / Jenkins Tools

| Tool | Version / Value | Notes |
| --- | --- | --- |
| Jenkins | `2.555.3 LTS` | Installed locally and running on `localhost:8080` |
| Jenkins Java | `JDK 21` | Selected during Jenkins installation |
| Local Java used by Maven wrapper | `21.0.9 LTS` | `mvn.cmd` sets `JAVA_HOME` to `C:\Program Files\Java\jdk-21` |
| Maven | `3.9.16` | Used through project wrapper `mvn.cmd` |
| Appium Server | `2.2.2` | Verified with `appium --version` |
| Appium UiAutomator2 Driver | `2.32.3` | Verified with `appium driver list --installed` |
| Android device | `7654a4f9` | Verified with `adb devices` |
| Appium server URL | `http://127.0.0.1:4723/` | Must match `config.properties` / Jenkins parameter |
| Node.js for Allure local server | `24.14.1` | Bundled under `.allure/node-v24.14.1-win-x64` |

## Project Dependencies

These are defined in `pom.xml`.

| Dependency / Plugin | Version |
| --- | --- |
| Java compiler release | `17` |
| Appium Java Client | `9.3.0` |
| Selenium Java / Selenium BOM | `4.23.0` |
| TestNG | `7.10.2` |
| ExtentReports | `5.1.2` |
| SLF4J Simple | `2.0.16` |
| Allure BOM | `2.34.0` |
| Allure TestNG | Managed by Allure BOM `2.34.0` |
| Allure Java Commons | Managed by Allure BOM `2.34.0` |
| Allure Maven Plugin | `3.0.1` |
| Maven Surefire Plugin | `3.2.5` |
| AspectJ Weaver | `1.9.24` |

## Reporting

| Report Tool | Version / Status | Notes |
| --- | --- | --- |
| ExtentReports Spark | `5.1.2` | Current primary report |
| Extent offline mode | Enabled | Helps Jenkins render CSS/JS assets |
| Allure Java | `2.34.0` | Previously used |
| Allure Maven Plugin | `3.0.1` | Previously used |
| Allure CLI | `3.4.1` | Local `.allure` CLI |

## Jenkins Plugins Used / Needed

| Jenkins Plugin | Purpose |
| --- | --- |
| Git | Pull code from GitHub |
| Pipeline | Run `Jenkinsfile` |
| JUnit | Publish Surefire XML test results |
| HTML Publisher | Show Extent report link inside Jenkins build |

## Mobile App Configuration

Defined in `src/main/resources/config.properties`.

| Key | Value |
| --- | --- |
| `platform.name` | `Android` |
| `device.name` | `Android Emulator` |
| `automation.name` | `UiAutomator2` |
| `app.package` | `com.snapwork.kotak` |
| `app.activity` | `com.snapwork.kotak.MainActivity` |
| `no.reset` | `true` |
| `full.reset` | `false` |
| `new.command.timeout` | `120` |
| `wait.short.seconds` | `3` |
| `wait.default.seconds` | `20` |
| `wait.login.seconds` | `30` |
| `wait.long.seconds` | `60` |
| `wait.dashboard.seconds` | `60` |
| `login.manual.wait.seconds` | `90` |

## Useful Version Check Commands

```powershell
java -version
.\mvn.cmd -version
adb devices
appium --version
appium driver list --installed
```

## Notes

- Jenkins LTS `2.555.3` requires compatible Java such as JDK 21.
- The Maven project compiles with Java release `17`, so running Jenkins on JDK 21 is fine.
- Appium must be started with a base path matching the framework URL.
- Recommended Appium start command:

```powershell
appium --base-path /
```
