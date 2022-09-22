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
                sh "kubectl config use-context deployment"
                sh "kubectl apply -f hazelcast.yaml"
                sh "kubectl apply -f deployment.yaml"
                sh "kubectl apply -f service.yaml"
                  //sh "docker run -d --rm -p 7000:7000 --name calculator zeemodevops/simomere:calculator-${BUILD_TIMESTAMP}"
            }
           /* post {
                always {
                    sh "docker stop calculator"
                }
            } */
        }
         stage("Acceptance test") {
            steps{
                sleep 60
                sh "chmod +x acceptance_test.sh && ./acceptance_test.sh"
                //sh "./gradlew acceptanceTest -Dcalculator.url=http://192.168.1.55:7000"
            }
            post {
                always {
                    sh "kubectl delete deployment calculator-deployment"
                    sh "kubectl delete deployment hazelcast"
                    sh "kubectl delete service calculator-service"
                    sh "kubectl delete service hazelcast"
                }
            }
        }
        stage("Deploy to production") {
            steps{
                  sleep 60
                  sh "kubectl config use-context deployment"
                  sh "kubectl apply -f hazelcast.yaml"
                  sh "kubectl apply -f deployment.yaml"
                  sh "kubectl apply -f service.yaml"
            }
        }
        stage("Smoke test"){
            steps{
                sleep 60
                sh 'chmod +x smoke-test.sh && ./smoke-test.sh'
            }
        }
    }
}
