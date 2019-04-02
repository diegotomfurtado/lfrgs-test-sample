
node{

        stage('Build') {

            gradle 'clean'
            gradle 'functionalTest'
        }

        stage('Tests') {

            echo 'Testing...'

                git url: 'https://github.com/diegotomfurtado/lfrgs-test-sample.git'


        }

        stage('Deploy') {

            echo 'Deploying...'
        }
}
