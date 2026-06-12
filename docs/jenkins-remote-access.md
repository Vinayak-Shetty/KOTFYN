# Jenkins Remote Access Guide

This guide explains how to access Jenkins from another system on the same network.

## During Jenkins Installation

On the Jenkins **Custom Setup** screen, enable:

```text
Firewall Exception
```

If it shows a red X:

1. Click the red X beside **Firewall Exception**
2. Select:

```text
Will be installed on local hard drive
```

This allows other systems on the network to access Jenkins through Windows Firewall.

## Find Jenkins Machine IP Address

On the machine where Jenkins is installed, run:

```powershell
ipconfig
```

Find the IPv4 address.

Example:

```text
IPv4 Address . . . . . . . . . . . : 192.168.1.25
```

## Open Jenkins From Another System

On another system connected to the same network, open:

```text
http://<jenkins-machine-ip>:8080
```

Example:

```text
http://192.168.1.25:8080
```

## If Jenkins Does Not Open Remotely

Allow port `8080` manually in Windows Firewall.

Open PowerShell as Administrator and run:

```powershell
New-NetFirewallRule -DisplayName "Jenkins 8080" -Direction Inbound -Protocol TCP -LocalPort 8080 -Action Allow
```

Then try again from the other system:

```text
http://<jenkins-machine-ip>:8080
```

## Important Notes

- Remote users can open Jenkins UI and trigger builds.
- The actual automation still runs on the Jenkins machine.
- Appium must run on the Jenkins machine.
- Android device/emulator must be connected to the Jenkins machine.
- The Jenkins machine and remote system should be on the same network.
- If using office VPN or restricted Wi-Fi, firewall/network rules may block access.

## Quick Checklist

On Jenkins machine:

```powershell
Start-Service Jenkins
appium --base-path /
adb devices
```

From another system:

```text
http://<jenkins-machine-ip>:8080
```

