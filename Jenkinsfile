pipeline {
    agent any

    tools {
        jdk 'Java17'
        maven 'Maven 3.9.11'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git 'https://github.com/asingh403/AUGAPIFW2025.git'
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
        
        stage('Clean Allure Reports') {
            steps {
                sh 'rm -rf target/allure-results/*'
                sh 'rm -rf allure-report/*'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn test -Dsurefire.suiteXmlFiles=gorest.xml'
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
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'target/allure-results']]
                ])
            }
        }
        
        stage('Publish Reports') {
            steps {
                archiveArtifacts artifacts: '**/target/*.jar'
            }
        }
    }
}