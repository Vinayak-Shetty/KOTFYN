param(
    [Parameter(Mandatory = $true)]
    [string] $ReportDir,

    [Parameter(Mandatory = $true)]
    [string] $NodeExe,

    [Parameter(Mandatory = $true)]
    [string] $ServerScript
)

$resolvedReportDir = (Resolve-Path -LiteralPath $ReportDir).Path
$serverRoot = $resolvedReportDir
$indexFile = Join-Path $resolvedReportDir "index.html"
$mavenIndexFile = Join-Path $resolvedReportDir "allure-maven.html"
$awesomeIndexFile = Join-Path $resolvedReportDir "awesome\index.html"
$statisticFile = Join-Path $resolvedReportDir "widgets\statistic.json"
$awesomeStatisticFile = Join-Path $resolvedReportDir "awesome\widgets\statistic.json"
$healthPath = "widgets/statistic.json"

if (Test-Path -LiteralPath $awesomeIndexFile) {
    $indexFile = $awesomeIndexFile
    $serverRoot = Join-Path $resolvedReportDir "awesome"
}

if (-not (Test-Path -LiteralPath $indexFile) -and (Test-Path -LiteralPath $mavenIndexFile)) {
    Copy-Item -LiteralPath $mavenIndexFile -Destination $indexFile -Force
}

if (-not (Test-Path -LiteralPath $indexFile)) {
    throw "Allure index.html was not found in: $resolvedReportDir"
}

if (-not (Test-Path -LiteralPath $statisticFile) -and (Test-Path -LiteralPath $awesomeStatisticFile)) {
    $statisticFile = $awesomeStatisticFile
    $healthPath = "widgets/statistic.json"
}

if (-not (Test-Path -LiteralPath $statisticFile)) {
    throw "Allure statistic.json was not found in: $statisticFile"
}

$statistic = Get-Content -LiteralPath $statisticFile -Raw
Write-Output "Allure statistic: $statistic"

Get-CimInstance Win32_Process |
    Where-Object { $_.Name -like 'node*' -and $_.CommandLine -like '*serve-report.js*' } |
    ForEach-Object {
        try {
            Stop-Process -Id $_.ProcessId -Force
        } catch {
            Write-Output "Unable to stop existing report server $($_.ProcessId): $($_.Exception.Message)"
        }
    }

$listener = [System.Net.Sockets.TcpListener]::new([System.Net.IPAddress]::Loopback, 0)
$listener.Start()
$port = $listener.LocalEndpoint.Port
$listener.Stop()

$url = "http://127.0.0.1:$port/details.html"
$healthUrl = "http://127.0.0.1:$port/$healthPath"
$serverLog = Join-Path $resolvedReportDir "report-server.log"
$serverErrorLog = Join-Path $resolvedReportDir "report-server-error.log"

$nodeArguments = @(
    "`"$ServerScript`"",
    "`"$serverRoot`"",
    "$port"
) -join " "

Start-Process `
    -WindowStyle Hidden `
    -FilePath $NodeExe `
    -ArgumentList $nodeArguments `
    -RedirectStandardOutput $serverLog `
    -RedirectStandardError $serverErrorLog

$ready = $false
for ($attempt = 0; $attempt -lt 30; $attempt++) {
    try {
        Invoke-WebRequest -Uri $healthUrl -UseBasicParsing -TimeoutSec 1 | Out-Null
        $ready = $true
        break
    } catch {
        Start-Sleep -Milliseconds 500
    }
}

if (-not $ready) {
    Write-Output "Allure report server did not start."
    Write-Output "Server log: $serverLog"
    Write-Output "Server error log: $serverErrorLog"
    exit 1
}

Write-Output "Allure report URL: $url"
Start-Process $url
