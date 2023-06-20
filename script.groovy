def buildApp()
	{
	echo 'Building the application1'
	}
def testApp()
	{
	echo 'Testing the application1'
	}

def deployApp()
	{
	echo 'Deploying the application to Production'
	echo ' Deploying vesrion ${params.version} '
	}

return this
