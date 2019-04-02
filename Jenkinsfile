
node{

        stage('Build') {

            gradle 'clean'
            gradle 'functionalTest'
        }

        stage('Unit Testing') {

            echo 'Unit Testing...'

            gradle 'unitTest'
        }

        stage('Integration Testing') {

            echo 'Integration Testing...'

            gradle 'functionalTest'

            checkout scm

            echo 'Testing 001'
            sh 'make'
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true

            echo 'Testing 002'
            sh './gradle clean sonarqube'
        }

        stage('Deploy') {

            echo 'Deploying...'
        }
}
