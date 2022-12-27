def ecrLoginHelper="docker-credential-ecr-login"
def region="ap-northeast-2"
def ecrUrl="101253377448.dkr.ecr.ap-northeast-2.amazonaws.com"
def repository="alc-diary"
def deployHost="13.125.231.232"
def SLACK_CHANNEL_NAME="#test"
def SLACK_START_COLOR="#FFFF00"
def SLACK_SUCCESS_COLOR="#2C953C"
def SLACK_FAIL_COLOR="#FF3232"

pipeline {
    agent any

    stages {
        stage('start') {
            steps {
                slackSend(
                    channel: SLACK_CHANNEL_NAME,
                    color: SLACK_START_COLOR,
                    message: '배포 시작'
                )
            }
        }
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
            post {
                success {
                    slackSend(
                        channel: SLACK_CHANNEL_NAME,
                        color: SLACK_SUCCESS_COLOR,
                        message: '배포 성공'
                    )
                }
                failure {
                    slackSend(
                        channel: SLACK_CHANNEL_NAME,
                        color: SLACK_FAIL_COLOR,
                        message: '배포 실패'
                    )
                }
            }
        }
    }
}
