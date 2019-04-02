
node{

        stage('Build') {

            gradle 'clean'
            gradle 'functionalTest'
        }

        stage('Tests') {

            echo 'Testing...'

            checkout scm

           sh "./gradlew clean sonarqube"


        }

        stage('Deploy') {

            echo 'Deploying...'
        }
}
