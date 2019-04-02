
node{

        stage('Build') {

            gradle 'clean'
            gradle 'functionalTest'
        }

        stage('Unit Testing') {

            echo 'Unit Testing...'

            gradle 'unitTest'
            sh 'make'
        }

        stage('Integration Testing') {

            echo 'Integration Testing...'

            gradle 'functionalTest'

            checkout scm

            echo 'Testing 001'
            sh 'make check || true'
            junit '**/target/*.xml'

            if (currentBuild.currentResult == 'SUCCESS') {
                stage('Deploy') {
                    sh 'make publish'
                }
            }

        }

        stage('Deploy') {

            echo 'Deploying...'
        }
}
