pipeline {
   agent any
   
   tools {
      jdk 'Java17'
      maven 'Maven_3.9.11'
   }
   
   stages {
      stage('Checkout Code') {
         steps {
            git 'https://github.com/asingh403/AUGAPIFW2025.git'
         }
      }
      
      stage('Build') {
         steps {
            sh 'mvn clean compile'
         }
      }
      
      stage('Clean Allure Reports') {
         steps {
            sh 'rm -rf target/allure-results/* || true'
            sh 'rm -rf allure-report/* || true'
         }
      }
      
      stage('Test Execution') {
         steps {
            git 'https://github.com/asingh403/AUGAPIFW2025.git'
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
