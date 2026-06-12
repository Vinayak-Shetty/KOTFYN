param(
    [string] $JenkinsHome = $env:JENKINS_HOME
)

if ([string]::IsNullOrWhiteSpace($JenkinsHome)) {
    $defaultHome = "C:\ProgramData\Jenkins\.jenkins"
    if (Test-Path -LiteralPath $defaultHome) {
        $JenkinsHome = $defaultHome
    } else {
        throw "JENKINS_HOME is not set and default path was not found: $defaultHome"
    }
}

$source = Join-Path $PSScriptRoot "init.groovy.d\disable-html-report-csp.groovy"
$targetDirectory = Join-Path $JenkinsHome "init.groovy.d"
$target = Join-Path $targetDirectory "disable-html-report-csp.groovy"

if (-not (Test-Path -LiteralPath $source)) {
    throw "Source file not found: $source"
}

New-Item -ItemType Directory -Path $targetDirectory -Force | Out-Null
Copy-Item -LiteralPath $source -Destination $target -Force

Write-Output "Installed Jenkins HTML report CSP override:"
Write-Output $target
Write-Output "Restart Jenkins for this to take effect."

