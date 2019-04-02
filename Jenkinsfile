
node{

        stage('Build') {

            gradle 'clean'
            gradle 'functionalTest'
        }

        stage('Tests') {

            echo 'Testing...'

                git url: 'https://github.com/diegotomfurtado/lfrgs-test-sample.git'
                def mvnHome = tool 'M3'
                sh "${mvnHome}/bin/mvn -B -Dmaven.test.failure.ignore verify"
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
                junit **/target/surefire-reports/TEST-*.xml'
            
        }

        stage('Deploy') {

            echo 'Deploying...'
        }
}
