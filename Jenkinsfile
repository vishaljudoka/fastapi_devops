def gv

pipeline {

	agent any
	environment
	{
		server-cred = credentials('docker-hub-login')
		docker-tag = "vishaljudoka/vksh"
		dockerImage = ''
	}

	paramaters {

		string ( name ="JenkinPipeline-Declarative" ,defaultvalue="JenkinPipeline-Declarative 1.0" , description="JenkinPipeline-Declarative" )
		choice( name ="version" ,choices : [] ,description="Build version number" )
		booleanParam ( name = "executeTests" ,defaultvalue=true , description="Execute test" )



	stages {

	    stage('Init')
		{
			steps {
				script {
				    gv= load "script.groovy"
						}

			      }

		}

		stage('GIT checkout')
		{
			steps {
					git branch: 'main', url:'https://github.com/vishaljudoka/fastapi_devops.git'

			      }

		}

		stage('Build Image')
		{
			steps {
			script {

				img = docker-tag + ":${env.BUILD_ID}"
				println ("${img}")
				dockerImage = docker.build("${img}")

					}


			      }

		}

		stage ('Stop previous running container on Test'){
            steps{
                sh returnStatus: true, script: 'docker stop $(docker ps -a | grep ${JOB_NAME} | awk \'{print $1}\')'
                sh returnStatus: true, script: 'docker rmi $(docker images | grep ${registry} | awk \'{print $3}\') --force' //this will delete all images
                sh returnStatus: true, script: 'docker rm ${JOB_NAME}'
            }


		stage('Deploy on Test Environment')
		{
			when {
				expression {
				params.executeTests
				}
				}
			steps {
					bat "docker run -d --name ${JOB_NAME} =p 8000:8000 ${img} "


			      }

		}

		stage('Push code to DockerHub')
		{
			steps {
				script {
					withCredentials([usernamePassword(credentialsId: 'server-cred', passwordVariable: 'passw', usernameVariable: 'user')])
						{
						echo "${user}"
						echo "${passw}"
						}
					docker.withRegistry ( 'https://registry.hub.docker.com ', server-cred) {
						dockerImage.push()

					}

			      }

		}

		stage('Deploy to Production')
		{
			steps {
				script {
					gv.deployApp()
					}

			      }


		}




	}

  }


}