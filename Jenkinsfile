
node{

        stage('Checkout') {
            checkout scm
        }

        stage('Setup') {

            gradle clean
        }

        stage('Build') {

                gradle build unitTest

        }
}
