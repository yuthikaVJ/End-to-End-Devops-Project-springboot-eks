pipeline {
    agent {
        docker {
            image 'yuthikavj31545/docker-agent:v1'
            args '--user root -v /var/run/docker.sock:/var/docker.sock'
        }  
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
        stage('Build Docker Image'){
            steps {
                 sh 'docker build -t springboot-app:latest -f ./springboot-app/Dockerfile ./springboot-app'
            }
        }

    }
}