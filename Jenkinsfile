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
      stage('SonarQube Analysis') {
         steps {
            withSonarQubeEnv('MySonarQubeServer') {
               sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=AUGAPIFW2025 -Dsonar.projectName=AUGAPIFW2025'
            }
         }
      }
      stage('Quality Gate') {
         steps {
            timeout(time: 1, unit: 'MINUTES') {
               waitForQualityGate abortPipeline: true
            }
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
        sh '''
            find . -name "*.properties" \
              -exec echo "=== {} ===" \; \
              -exec cat {} \;
        '''
        sh 'grep -r "bearertoken" . || true'
    }
}

      
      stage('Test Execution') {
         steps {
            sh 'mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testrunners/gorest.xml || true'
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