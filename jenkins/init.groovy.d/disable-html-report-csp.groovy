/*
 * Allows Jenkins HTML Publisher/archived HTML reports such as ExtentReports
 * to load their CSS and JavaScript correctly.
 *
 * Copy this file to:
 *   %JENKINS_HOME%\init.groovy.d\disable-html-report-csp.groovy
 *
 * Then restart Jenkins.
 */
System.setProperty("hudson.model.DirectoryBrowserSupport.CSP", "")

