
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
            
            junit '**/target/*.xml'

        }

        stage('Deploy') {

            echo 'Deploying...'

            if (currentBuild.result == null || currentBuild.result == 'SUCCESS') {
                stage('Deploy') {
                    sh 'make publish'
                }
            }
        }
}
