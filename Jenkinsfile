def img
pipeline {
    environment {
        registry = "vksh/python-jenkins" //To push an image to Docker Hub, you must first name your local image using your Docker Hub username and the repository name that you created through Docker Hub on the web.
        registryCredential = 'docker-hub-login'
        dockerImage = ''
    }
    agent any
    stages {
        stage('checkout') {
            steps {
                git 'https://github.com/vishaljudoka/fastapi_devops.git'
            }
        }


        stage('Build Image') {
            steps {
                script {
                    img = registry + ":${env.BUILD_ID}"
                    println ("${img}")
                    dockerImage = docker.build("${img}")
                }
            }
        }



        stage('Test - Run Docker Container on Jenkins node') {
           steps {

                bat "docker run -d --name ${JOB_NAME} -p 8000:8000 ${img}"
          }
        }


    }

}