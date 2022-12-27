def ecrLoginHelper="docker-credential-ecr-login"
def region="ap-northeast-2"
def ecrUrl="101253377448.dkr.ecr.ap-northeast-2.amazonaws.com"
def repository="alc-diary"
def deployHost="13.125.231.232"

pipeline {
    agent any

    stages {
        stage('Pull Codes from Github') {
            steps {
                checkout scm
            }
        }
        stage('Build Codes by Gradle') {
            steps {
                sh """
                ./gradlew clean build
                """
            }
        }
        stage('Dockerizing project by dockerfile') {
            steps {
                sh """
                    docker build -t alc-diary .
                    docker tag alc-diary:latest alc-diary:${currentBuild.number}
                """
            }
        }
        stage('Push to AWS ECR Repository') {
            steps {
                script {
                    docker.withRegistry("https://${ecrUrl}", "ecr:${region}:aws-key") {
                        docker.image("alc-diary:${currentBuild.number}").push()
                    }
                }
//                 withAWS(region:"${region}", credentials:"aws-key") {
//                     ecrLogin()
//                     sh """
//                         curl -O https://amazon-ecr-credential-helper-releases.s3.ap-northeast-2.amazonaws.com/0.4.0/linux-amd64/${ecrLoginHelper}
//                         chmod +x ${ecrLoginHelper}
//                         mv ${ecrLoginHelper} /usr/local/bin/
//                         docker build -t alc-diary .
//                         docker tag alc-diary:latest 101253377448.dkr.ecr.ap-northeast-2.amazonaws.com/alc-diary:${currentBuild.number}
//                         docker push 101253377448.dkr.ecr.ap-northeast-2.amazonaws.com/alc-diary:${currentBuild.number}
//                      """
//                 }
            }
        }
        stage('Deploy to AWS EC2 VM') {
            steps {
                sshagent(credentials : ["alc-diary-key"]) {
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@${deployHost} \
                     'aws ecr get-login-password --region ${region} | docker login --username AWS --password-stdin ${ecrUrl}/${repository}; \
                      docker run -d -p 80:8080 -t ${ecrUrl}/${repository}:${currentBuild.number};'"
                }
            }
        }
    }
}
