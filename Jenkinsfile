pipeline {
    agent any

    tools {
        jdk 'JDK-17'
        maven 'Apache Maven 3.9.11'
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
        
        stage('Test') {
            steps {
                sh 'mvn test'
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