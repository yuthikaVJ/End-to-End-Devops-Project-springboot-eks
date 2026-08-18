pipeline {
    agent {
        docker {
            image 'yuthikavj31545/docker-agent:v1'
            args '--user root -v /var/run/docker.sock:/var/run/docker.sock'
        }  
    }

    environment {
        DOCKER_IMAGE = "yuthikavj31545/spring-boot-app:${BUILD_NUMBER}"
        SONARQUBE_URL = "http://13.212.167.198:9000"

        GIT_REPO_NAME = "End-to-End-Devops-Project-springboot-eks"
        GIT_USERNAME = "yuthikaVJ"

        HELM_VALUES_FILE = "End-to-End-Devops-Project-springboot-eks/blob/main/helm/spring-boot-app/values.yaml"
    }
    stages {

        stage('Checkout'){
            steps {
                sh 'echo passed'
            }
        }

        stage('Build and Test'){
            steps {
                sh 'ls -ltr'
                sh 'cd springboot-app && mvn clean package'
            }
        }

        stage('Static Code Analysis'){
            steps {
                withCredentials([
                    string(
                        credentialsId: 'sonarqube',
                        variable: 'SONAR_AUTH_TOKEN'
                    )
                ]){
                    sh 'cd springboot-app'
                    sh   ' mvn sonar: sonar  -Dsonar.projectKey=springboot-app   -Dsonar.login=$SONAR_AUTH_TOKEN   -Dsonar.host.url=${SONARQUBE_URL} '
                        
                }
            }
        }


        stage('Build Docker Image'){
            steps {
                
                   sh ' cd springboot-app && docker build -t ${DOCKER_IMAGE} .'
                    
                
                 //sh 'docker build -t springboot-app:latest -f ./springboot-app/Dockerfile ./springboot-app'
            }
        }

        stage('Push Docker Image'){
            steps {
                script {
                    def dockerImage = docker.image("${DOCKER_IMAGE}")
                    docker.withRegistry (
                        'https://index.docker.io/v1/',
                        'docker-cred'
                    ){
                        dockerImage.push()
                    }

                }
            }
        }

    }
}