
node{

        stage('Checkout') {
            checkout scm
        }

        stage('Setup') {

            gradlew 'clean'
        }

        stage('Build') {

                gradlew 'build -x unitTest'

        }
}
