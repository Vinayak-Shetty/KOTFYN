@echo off
setlocal

set "JAVA_HOME=C:\Program Files\Java\jdk-22"
set "MAVEN_HOME=%~dp0.tools\apache-maven-3.9.16"
call "%MAVEN_HOME%\bin\mvn.cmd" %*
