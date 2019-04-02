
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

        

    } finally {
        stage('Cleanup') {
            dir(workspace) {
                deleteDir();
            }
        }
    }
}
