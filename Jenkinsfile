#!groovy
import groovy.transform.Field

node{

        stage('Checkout') {
            checkout scm
        }

        stage('Setup') {

            gradle clean
        }

}
