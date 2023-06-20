def gv

pipeline {

    agent any
	environment
		{
		server_cred=credentials('servercred')
		}
		parameters {
			string ( name="JenkinPipeline-Declarative" , defaultvalue= "JenkinPipeline-Declarative 1.0" , description="JenkinPipeline-Declarative" )
			choice ( name="version" , choices : [1.1.0 , 1.1.1 , 1.1.2 ], description="version build number" )
			booleanParam ( name = "executeTests" , defaultvalue= true , description="execute test" )
	stages
    {
		stage ("init") {
            steps
            {
                script
				{
				gv= load "script.groovy"
				}
            }

		}
        stage ("Build") {
            steps
            {
                script
				{
				gv.buildApp()
				}

				withCredentials([usernamePassword(credentialsId: 'servercred', passwordVariable: 'passw', usernameVariable: 'user')])
				{
						echo "${user}"
						echo "${passw}"
						}
            }

         }

        stage ("Test"){

			when {
				expression {
				params.executeTests
				}

				}
            steps
            {
                script
				{
				gv.testApp()
				}
            }

        }

        stage ("deploy"){

            steps
             {
               script
				{
				gv.deployApp()
				}
             }

    }

    }

}
