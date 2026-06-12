# Git Checklist Before Jenkins

This document explains the important Git steps to do after adding ExtentReports and before connecting the project to Jenkins.

## Why This Step Is Needed

Jenkins needs access to the latest project code.

Since we added ExtentReports, Jenkins support, and documentation locally, those files must be committed and pushed to GitHub before Jenkins can use them.

## Important Changes Added

The main changes made before Jenkins setup are:

- ExtentReports dependency added in `pom.xml`
- Extent report listener added
- Extent step logging added for `RegistrationSteps`
- `run-test-with-report.cmd` updated to generate Extent report
- `OPEN_REPORT=false` support added for Jenkins
- `Jenkinsfile` added
- Jenkins setup documentation added
- Git/GitHub documentation added

## Files To Expect In Git

Important files that should be committed:

```text
pom.xml
run-test-with-report.cmd
Jenkinsfile
docs/jenkins-setup.md
docs/git-github-guide.md
docs/git-before-jenkins-checklist.md
src/main/java/com/mobile/automation/config/ConfigReader.java
src/test/java/com/mobile/automation/base/BaseTest.java
src/test/java/com/mobile/automation/listeners/ExtentTestListener.java
src/test/java/com/mobile/automation/reporting/ExtentReportManager.java
src/test/java/com/mobile/automation/reporting/ExtentStepLogger.java
src/test/java/com/mobile/automation/steps/RegistrationSteps.java
```

## Before Committing

Run compile check:

```powershell
.\mvn.cmd -DskipTests compile test-compile
```

Expected result:

```text
BUILD SUCCESS
```

Also check that no generated report/build folders are being committed.

Do not commit:

```text
target/
.allure/
.tools/
.vscode/
```

## Check Git Status

```powershell
git status
```

What to verify:

- New Extent/Jenkins files are listed
- No unwanted generated files are listed
- No sensitive data is accidentally included

## Add Project Changes

```powershell
git add .
```

This stages all project changes for commit.

## Commit Changes

Recommended commit message:

```powershell
git commit -m "Add Extent reporting and Jenkins pipeline setup"
```

This creates a local Git checkpoint containing the Extent report and Jenkins setup work.

## Push To GitHub

If remote is already configured:

```powershell
git push
```

If this is the first push:

```powershell
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git
git push -u origin main
```

Replace the URL with your actual GitHub repository URL.

## After Push

Confirm on GitHub that these files are visible:

```text
Jenkinsfile
pom.xml
run-test-with-report.cmd
docs/jenkins-setup.md
src/test/java/com/mobile/automation/reporting/ExtentReportManager.java
src/test/java/com/mobile/automation/listeners/ExtentTestListener.java
```

Once these are visible on GitHub, Jenkins can be configured using:

```text
Pipeline script from SCM
```

and script path:

```text
Jenkinsfile
```

## Important Note

Before pushing to GitHub, review:

```text
src/main/resources/config.properties
```

Do not push real passwords, real MPINs, real OTPs, production test data, or internal secrets.

Use a private repository if this project contains company/internal code.

