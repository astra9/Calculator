pipeline {
    agent any
    triggers {
        pollSCM('* * * * *')
    }
    stages {
        stage("Compile"){
            steps {
                sh "./gradlew compileJava"
            }
        }
        stage("Unit test"){
            steps{
                sh "./gradlew test"
            }
        }
        stage("Code coverage"){
            steps {
                sh "./gradlew jacocoTestReport"
                publishHTML (target: [
                    reportDir: 'build/reports/jacoco/test/html',
                    reportFiles: 'index.html',
                    reportName: 'JaCoCo Report'
                ])
                sh "./gradlew jacocoTestCoverageVerification"
            }
        }
        stage("Static code analysis"){
            steps {
                sh "./gradlew checkstyleMain"
                publishHTML (target: [
                    reportDir: 'build/reports/checkstyle/',
                    reportFiles: 'main.html',
                    reportName: 'Checkstyle Report'
                ])
            }
        }
        stage("Package"){
            steps {
                sh "./gradlew build"
            }
        }
        stage("Docker build"){
            steps {
                sh "docker build -t zeemodevops/simomere:calculator-${BUILD_TIMESTAMP} ."
            }
        }
        stage("Docker push") {
            steps{
                withCredentials( [usernamePassword( credentialsId: 'Docker-Hub',
                                                  usernameVariable: 'USERNAME',
                                                  passwordVariable: 'PASSWORD')]) {
                    sh 'docker login --username $USERNAME --password $PASSWORD'
                    sh 'docker push zeemodevops/simomere:calculator-${BUILD_TIMESTAMP}'
                }
            }
        }
        stage("Update version"){
            steps{
                sh "sed  -i 's/{{VERSION}}/${BUILD_TIMESTAMP}/g' deployment.yaml"
            }
        }
        stage("Deploy to staging") {
            steps{
                sh 'kubectl create secret docker-registry regcred --docker-server=https://index.docker.io/v1/ --docker-username=$USERNAME --docker-password=$PASSWORD'
                sh "kubectl config use-context deployment"
                sh "kubectl apply -f hazelcast.yaml"
                sh "kubectl apply -f deployment.yaml"
                sh "kubectl apply -f service.yaml"
                // sh "docker run -d --rm -p 7777:7777 --name calculator zeemodevops/simomere:calculator-${BUILD_TIMESTAMP}"
            }
        }
        /* stage("Acceptance test") {
            steps{
                sleep 60
                sh "./gradlew acceptanceTest -Dcalculator.url=http://192.168.1.55:7777"
            }
            post {
                always {
                    sh "docker stop calculator"
                }
            }
        }
        */
    }
}
