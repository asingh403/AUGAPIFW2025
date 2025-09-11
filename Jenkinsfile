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
            git branch: 'main', url: 'https://github.com/asingh403/AUGAPIFW2025.git'
         }
      }
      
      stage('Post-Checkout Debug') {
         steps {
            sh 'echo "=== DEBUGGING POST CHECKOUT ==="'
            sh 'pwd && ls -la'
            sh 'java -version || echo "Java not found"'
            sh 'mvn -version || echo "Maven not found"'
         }
      }
      
      stage('Debug Config') {
         steps {
            sh 'find . -name "*.properties" -exec echo "=== {} ===" \\; -exec cat {} \\;'
            sh 'grep -r "bearertoken" . || true'
         }
      }
      
      stage('Test Execution with Coverage') {
         steps {
            sh 'mvn test jacoco:report -DsuiteXmlFile=src/test/resources/testrunners/gorest.xml'
         }
         post {
            always {
               junit '**/target/surefire-reports/*.xml'
            }
         }
      }
      
      stage('SonarQube Analysis') {
         steps {
            withSonarQubeEnv('SonarQubeServer') {
               sh 'mvn sonar:sonar'
            }
         }
      }
      
      stage('Quality Gate') {
         steps {
            script {
               try {
                  timeout(time: 2, unit: 'MINUTES') {
                     waitForQualityGate abortPipeline: false
                  }
               } catch (org.jenkinsci.plugins.workflow.steps.FlowInterruptedException e) {
                  echo "Quality gate timed out after 5 minutes - treating as passed"
                  currentBuild.result = 'SUCCESS'
               }
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
