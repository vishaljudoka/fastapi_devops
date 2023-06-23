def gv
def img

pipeline
    {

	agent any
	    environment
	        {
	            CLOUDSDK_CORE_PROJECT='vksh-04030613'
	            LOCATION='southamerica-east1-b'
                CLIENT_EMAIL='jenkins-gcloud@vksh-04030613.iam.gserviceaccount.com'
                GCLOUD_CREDS=credentials('gcloud-creds')
                CLUSTER_NAME='vksh-cluster'
		        hubcred='docker-hub-login'
		        hubtag="vishaljudoka/vksh"
		        Image=''
	        }

	    parameters
	        {

		        string ( name :"JenkinPipeline-Declarative" , defaultValue : "JenkinPipeline-Declarative 1.0" , description : "JenkinPipeline-Declarative" )
		    	choice ( name:"version" , choices : ['1.1.0' , '1.1.1' , '1.1.2' ], description : "version build number" )
			    booleanParam ( name : "executeTests" , defaultValue :  true , description : "execute test" )

            }

	stages
	    {

	       stage('GIT checkout')
                {
			        steps
			            {
			    		    git branch: 'main', url:'https://github.com/vishaljudoka/fastapi_devops.git'
        	            }
                }
            stage('Init')
		        {
			        steps
			        {
				        script
				            {
				                gv= load "script.groovy"
						    }

			        }
			    }

		    stage('Build Image')
		        {
		    	    steps
		    	        {
		    	            script
		    	                {
		    		               img = hubtag + ":${env.BUILD_ID}"
		    		               println ("${img}")
		    		               Image = docker.build("${img}")
		    		            }
                        }
                }

		    stage ('Stop previous running container on Test')
		        {
                    steps
                        {
                            echo "${JOB_NAME}"
                            /*bat returnStdout: true, script: "docker stop ${JOB_NAME}"
                            bat returnStdout: true, script: "docker rm  ${JOB_NAME}"
                            bat returnStdout: true, script: "docker image prune -a --force"*/
                        }
                }


		    stage('Deploy on Test Environment')
		        {
		    	    when
		    	        {
		    		        expression
		    		            {
		    		                params.executeTests
		    		            }
		    		    }
		    	    steps
		    	        {
		    			    bat "docker run -d --name ${JOB_NAME} -p 8000:8000 ${img} "
                        }
                }

		    stage('Push code to DockerHub')
		        {
		    	    steps
		    	        {
		    		        script
		    		            {
		    			            withCredentials([usernamePassword(credentialsId: 'servercred', passwordVariable: 'passw', usernameVariable: 'user')])
		    				        {
		    				            echo "this is my username : ${user}"
		    				            echo "this is my password : ${passw}"
		    				        }
		    			                docker.withRegistry ( 'https://registry.hub.docker.com ', hubcred)
		    			                {
		    				            Image.push()
		    				            }
                                }
    		    	    }
                }

           stage('getting Prod K8s Details')
                {
                    steps
                    {
                        bat "gcloud version"
                        bat "gcloud auth activate-service-account --key-file=$GCLOUD_CREDS"
                        bat 'gcloud compute zones list'

                    }
                }
            stage('Deploy to K8s Production')
		        {
			        steps{
                    echo 'Deployment started ...'
                    step([$class: 'KubernetesEngineBuilder', projectId: env.CLOUDSDK_CORE_PROJECT, clusterName: env.CLUSTER_NAME, location: env.LOCATION, manifestPattern: 'app.yaml', credentialsId: env.GCLOUD_CREDS, verifyDeployments: true])
                    echo "Deployment Finished ..."
                }
                }

        }
                post
                {

                    always {
                        // Let's wipe out the workspace before we finish!
                            echo "complete"
                            }
                    success {
                         echo "success"
                         /*
                         script
			        	        {
                        gv.sendEmail("Successful"); } */
                            }
                    unstable {
                        echo "unstable"
                    }
                    failure {
                         echo "failure"
                        /*script
			        	        {
                        gv.sendEmail("failure"); } */
                    }
                }


    }