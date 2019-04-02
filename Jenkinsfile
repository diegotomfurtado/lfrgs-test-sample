
node{

        stage('Build') {

            gradle 'clean'
            gradle 'functionalTest'
        }

        stage('Unit Testing') {

            echo 'Unit Testing...'

            gradle 'unitTest'
        }

        stage('Functional Testing') {

            echo 'Functional Testing...'

            gradle 'functionalTest'

            checkout scm

            sh './gradle clean sonarqube'
        }

        stage('Deploy') {

            echo 'Deploying...'
        }
}
