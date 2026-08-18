pipeline {
    agent {
        docker {
            image 'xxxx/docker-agent:v1'
            args '--user root -v /var/run/docker.sock:/var/run/docker.sock'
        }  
    }

    environment {
        DOCKER_IMAGE = "xxx/xxx:${BUILD_NUMBER}"
        SONARQUBE_URL = "http://13.212.167.198:9000"

        GIT_REPO_NAME = "End-to-End-Devops-Project-springboot-eks"
        GIT_USER_NAME = "xxx"

        HELM_VALUES_FILE = "helm/spring-boot-app/values.yaml"
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
                    sh ''' 
                     cd springboot-app
                       mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar  -Dsonar.projectKey=springboot-app   -Dsonar.login=$SONAR_AUTH_TOKEN   -Dsonar.host.url=${SONARQUBE_URL} '''
                        
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
        stage('Update Helm Values'){
            steps {
                echo "Updating Helm values file with new Docker image tag"
                sh '''
                   echo "Updating Helm values file with new Docker image tag"

                    sed -i "s/tag: .*/tag: ${BUILD_NUMBER}/" \
                    helm/spring-boot-app/values.yaml

                    cat helm/spring-boot-app/values.yaml
                '''
            }
        }
         stage('Commit and Push Helm Changes') {
            steps {
                withCredentials([
                    string(
                        credentialsId: 'github',
                        variable: 'GITHUB_TOKEN'
                    )
                ]) {
                    sh '''
                         echo "Current directory:"
                         pwd
                         git config --global --add safe.directory /var/lib/jenkins/workspace/eks

                        echo "Git status:"
                        git status
                        git config user.email "xxx@gmail.com"
                        git config user.name "xxx"

                        git add ${HELM_VALUES_FILE}

                        git commit \
                          -m "Update Helm image tag to ${BUILD_NUMBER}" \
                          || echo "No changes to commit"

                        git push \
                          https://${GITHUB_TOKEN}@github.com/${GIT_USER_NAME}/${GIT_REPO_NAME}.git \
                          HEAD:main
                    '''
                }
            }
        }


    }
}