
node{

        stage('Checkout') {
            checkout scm
        }

        stage('Setup') {

            gradle 'clean'
        }

        stage('Build') {

            gradle 'functionalTest'
        }

        stage('Test'){
            wrap([$class: 'Xvnc']) {
               gradle 'test functionalTest --continue'
            }
        }
}
