pipeline {
    agent any
    
    tools {
        jdk 'Java17'
        maven 'Maven_3.9.11'
    }
    
    stages {
        stage('Clean Workspace') {
            steps {
                deleteDir()
            }
        }

    stage('Checkout Code') {
            steps {
                git 'https://github.com/asingh403/AUGAPIFW2025.git'
            }
        }
        stage('Post-Checkout Debug') {
    steps {
        sh 'echo "=== DEBUGGING POST CHECKOUT ==="'
        sh 'pwd'
        sh 'ls -la'
        sh 'echo "Java check:"'
        sh 'java -version || echo "Java not found"'
        sh 'echo "Maven check:"'
        sh 'mvn -version || echo "Maven not found"'
    }
}
    stage('Debug Workspace') {
        steps {
            sh 'pwd'
            sh 'ls -la'
            sh 'java -version'
            sh 'mvn -version'
    }
}
    stage('Debug Config') {
        steps {
                sh 'find . -name "*.properties" -exec echo "=== {} ===" \\; -exec cat {} \\;'
                sh 'grep -r "bearertoken" . || true'
            }
        }
      
        stage('Test Execution') {
            steps {
                sh "mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testrunners/gorest.xml"
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
      
        stage('Publish Allure Reports') {
            steps {
                allure([
                    includeProperties: false,
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'target/allure-results']]
                ])
            }
        }
    }
}
