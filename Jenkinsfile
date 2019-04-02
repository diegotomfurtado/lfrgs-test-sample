
node("devOps") {
    try {
        stage('Checkout') {
            checkout scm
        }

        stage('Setup') {

            if (isUnix()) {
                println 'Cleaning up leaked Tomcat process...'
                sh "pkill -9 -f 'java.*tomcat.*start'  || echo 'No Tomcat process found.'"
            }

            def bundlesDir = new File("bundles");
            if (bundlesDir.exists())
                bundlesDir.deleteDir();

            gradlew 'clean'
        }

        stage('Build') {
            try {
                gradlew 'build -x test -x integrationTest -x unitTest'
            } catch (exc) {
                throw exc
            }
        }

        stage('Test') {
            try {
                wrap([$class: 'Xvnc']) {
                    gradlew 'test unitTest integrationTest functionalTest'
                }
            } catch (exc) {
                throw exc
            } finally {
                junit testResults: '**/build/test-results/test/*.xml', allowEmptyResults: true
                junit testResults: '**/build/test-results/unitTest/*.xml', allowEmptyResults: true
                junit testResults: '**/build/test-results/integrationTest/*.xml', allowEmptyResults: true
                junit testResults: '**/build/test-results/performTests/*.xml', allowEmptyResults: true
                archiveArtifacts artifacts: '**/build/test-results/functionalTest/attachments/**', fingerprint: true, allowEmptyArchive: true
            }
        }

    } finally {
        stage('Cleanup') {
            dir(workspace) {
                deleteDir();
            }
        }
    }
}
