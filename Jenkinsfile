pipeline {
    agent { label 'windows' }

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    parameters {
        string(
            name: 'TEST_NAME',
            defaultValue: 'LoginSmokeTest',
            description: 'Test class or method, for example LoginSmokeTest or LoginSmokeTest#TC_SMOKE_LOGIN_001_verifyLoginLandingScreenLoadsSuccessfully'
        )
        string(
            name: 'APPIUM_SERVER_URL',
            defaultValue: 'http://127.0.0.1:4723/',
            description: 'Appium server URL reachable from this Jenkins agent'
        )
        string(
            name: 'DEVICE_NAME',
            defaultValue: 'Android Emulator',
            description: 'Android device name capability'
        )
        string(
            name: 'MANUAL_WAIT_SECONDS',
            defaultValue: '30',
            description: 'Wait time for manual Net Banking, OTP, and MPIN steps'
        )
    }

    environment {
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-21'
        OPEN_REPORT = 'false'
        APPIUM_SERVER_URL = "${params.APPIUM_SERVER_URL}"
        DEVICE_NAME = "${params.DEVICE_NAME}"
        REGISTRATION_MANUAL_WAIT_SECONDS = "${params.MANUAL_WAIT_SECONDS}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Environment Check') {
            steps {
                bat '"%JAVA_HOME%\\bin\\java.exe" -version'
                bat '.\\mvn.cmd -version'
                bat 'adb devices'
                bat 'appium --version'
            }
        }

        stage('Compile') {
            steps {
                bat '.\\mvn.cmd -DskipTests compile test-compile'
            }
        }

        stage('Run Tests') {
            steps {
                bat ".\\run-test-with-report.cmd ${params.TEST_NAME}"
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            archiveArtifacts allowEmptyArchive: true, artifacts: 'target/extent-report/**/*,target/extent-reports/**/*,target/surefire-reports/**/*'
            publishHTML([
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/extent-report',
                reportFiles: 'index.html',
                reportName: 'Extent Report',
                reportTitles: 'Extent Report'
            ])
        }
    }
}
