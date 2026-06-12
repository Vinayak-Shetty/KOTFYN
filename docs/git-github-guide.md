# Git and GitHub Guide

This guide explains how to push this project to GitHub for the first time and what each Git command does.

## Git vs GitHub

**Git** is the version control tool installed on your machine. It tracks file changes, commits, branches, and history.

**GitHub** is a website/server where you store your Git repository online. Jenkins can later pull the code from GitHub.

In simple words:

```text
Git = local tracking tool
GitHub = online place to store/share the code
```

## Important Before Pushing

Check `src/main/resources/config.properties` before pushing.

This file may contain test data or sensitive values:

```properties
registration.netbanking.password=...
registration.otp=...
registration.mpin=...
```

Do not push real passwords, real OTPs, real MPINs, production data, or internal secrets to a public repository.

Recommended:

- Create the GitHub repository as **Private**
- Replace sensitive values with dummy test values
- Use Jenkins/environment variables for sensitive values later

## Step 1: Create Repository On GitHub

1. Open GitHub in browser.
2. Click **New repository**.
3. Enter repository name, for example:

```text
kotak-fyn-mobile-automation
```

4. Select **Private**.
5. Do not select README, `.gitignore`, or license.
6. Click **Create repository**.
7. Copy the repository URL.

Example:

```text
https://github.com/YOUR_USERNAME/kotak-fyn-mobile-automation.git
```

## Step 2: Open Terminal In Project Folder

Open PowerShell inside this project folder:

```text
C:\Users\VinayakaShetty\OneDrive - Snapwork Technologies Private Limited\Desktop\Auomation Project\Kotak Corporate FYN
```

## Step 3: Initialize Git

```powershell
git init
```

What it does:

- Creates a hidden `.git` folder
- Makes this project a Git repository
- Starts tracking history locally

## Step 4: Check Current Status

```powershell
git status
```

What it does:

- Shows changed files
- Shows untracked files
- Shows files staged for commit

At first, most files will appear as untracked.

## Step 5: Add Files

```powershell
git add .
```

What it does:

- Stages all project files for commit
- The dot `.` means current folder and everything inside it

Use this when you want to include all current changes.

## Step 6: Commit Files

```powershell
git commit -m "Initial automation framework with Extent reports and Jenkins pipeline"
```

What it does:

- Saves a snapshot of staged files in local Git history
- `-m` provides the commit message

A commit is like a checkpoint.

## Step 7: Rename Branch To Main

```powershell
git branch -M main
```

What it does:

- Renames the current branch to `main`
- `main` is the standard default branch name used by GitHub

## Step 8: Connect Local Repo To GitHub

```powershell
git remote add origin https://github.com/YOUR_USERNAME/kotak-fyn-mobile-automation.git
```

What it does:

- Adds GitHub as the remote repository
- `origin` is the common name for the main remote

Replace the URL with your actual GitHub repository URL.

## Step 9: Push Code To GitHub

```powershell
git push -u origin main
```

What it does:

- Uploads local commits to GitHub
- Pushes the `main` branch
- `-u` links your local `main` branch with GitHub `main`

After this, future pushes can be done with:

```powershell
git push
```

## Full Command List For First Push

Run these from the project root:

```powershell
git init
git status
git add .
git commit -m "Initial automation framework with Extent reports and Jenkins pipeline"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/kotak-fyn-mobile-automation.git
git push -u origin main
```

## Daily Git Commands After Setup

After making code changes:

```powershell
git status
git add .
git commit -m "Your commit message"
git push
```

Explanation:

- `git status`: check changed files
- `git add .`: stage changes
- `git commit -m "...":` save local checkpoint
- `git push`: upload to GitHub

## Pull Latest Code

If code changes are made from another machine or by another team member:

```powershell
git pull
```

What it does:

- Downloads latest changes from GitHub
- Merges them into your local branch

## Check Remote URL

```powershell
git remote -v
```

What it does:

- Shows which GitHub repository this local project is connected to

Example output:

```text
origin  https://github.com/YOUR_USERNAME/kotak-fyn-mobile-automation.git (fetch)
origin  https://github.com/YOUR_USERNAME/kotak-fyn-mobile-automation.git (push)
```

## Common Errors

### Not a Git repository

Error:

```text
fatal: not a git repository
```

Meaning:

- You are not inside a Git initialized folder
- Run `git init`, or open terminal in the correct project folder

### Authentication failed

GitHub does not accept account password for Git push.

Use one of these:

- GitHub browser login popup from Git Credential Manager
- GitHub Personal Access Token

### Remote origin already exists

Error:

```text
remote origin already exists
```

Check current remote:

```powershell
git remote -v
```

Change remote URL:

```powershell
git remote set-url origin https://github.com/YOUR_USERNAME/kotak-fyn-mobile-automation.git
```

## Jenkins Connection

Once code is pushed to GitHub:

1. Create Jenkins Pipeline job.
2. Select **Pipeline script from SCM**.
3. Choose **Git**.
4. Paste GitHub repository URL.
5. Set script path:

```text
Jenkinsfile
```

Jenkins will then pull this project from GitHub and run the pipeline.

