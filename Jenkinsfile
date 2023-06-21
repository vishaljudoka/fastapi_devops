def gv
def img

pipeline
    {

	agent any
	    environment
	        {
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
            stage('Deploy to Production')
		        {
			        steps
			            {
			        	    script
			        	        {
			        		        gv.deployApp()
			        		    }
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
                    script
			        	        {
                        gv.sendEmail("Successful"); }
                            }
                    unstable {
                        script
			        	        {
                        gv.sendEmail("unstable"); }
                    }
                    failure {
                        script
			        	        {
                        gv.sendEmail("failure"); };
                    }
                }


    }