pipeline {
   agent any
   
   tools {
      maven 'Maven'
   }
   
   stages {
      stage('Checkout Code') {
         steps {
            git 'https://github.com/naveenanimation20/Jan2025APIFramework.git'
         }
      }
      
      stage('Deploy to Dev') {
         steps {
            echo 'Deploying to Dev environment...'
         }
      }
      
      stage('Run Sanity Tests on Dev') {
         steps {
            script {
               def status = sh(
                  script: "mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testrunners/testng_sanity.xml -Denv=prod",
                  returnStatus: true
               )
               if (status != 0) {
                  currentBuild.result = 'UNSTABLE'
               }
            }
         }
      }
      
      stage('Deploy to QA') {
         steps {
            echo 'Deploying to QA environment...'
         }
      }
      
      stage('Run Regression Tests on QA') {
         steps {
            script {
               def status = sh(
                  script: "mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testrunners/gorest.xml",
                  returnStatus: true
               )
               if (status != 0) {
                  currentBuild.result = 'UNSTABLE'
               }
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
      
      stage('Deploy to Stage') {
         steps {
            echo 'Deploying to Stage environment...'
         }
      }
      
      stage('Run Sanity Tests on Stage') {
         steps {
            script {
               def status = sh(
                  script: "mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testrunners/testng_sanity.xml -Denv=prod",
                  returnStatus: true
               )
               if (status != 0) {
                  currentBuild.result = 'UNSTABLE'
               }
            }
         }
      }
      
      stage('Publish Sanity ChainTest Report') {
         steps {
            publishHTML([
               allowMissing: false,
               alwaysLinkToLastBuild: false,
               keepAll: true,
               reportDir: 'target/chaintest',
               reportFiles: 'Index.html',
               reportName: 'HTML API Sanity ChainTest Report',
               reportTitles: ''
            ])
         }
      }
      
      stage('Deploy to Prod') {
         steps {
            echo 'Deploying to Prod environment...'
         }
      }
      
      stage('Run Sanity Tests on Prod') {
         steps {
            script {
               def status = sh(
                  script: "mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testrunners/testng_sanity.xml -Denv=prod",
                  returnStatus: true
               )
               if (status != 0) {
                  currentBuild.result = 'UNSTABLE'
               }
            }
         }
      }
   }
}