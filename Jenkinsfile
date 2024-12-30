pipeline{
    agent{ dockerfile true }
    stages{
        stage("Checkout"){
            steps{
                sh 'echo passed'
                //git branch: 'master', url: 'https://github.com/surya-kiran12/dev-stage.git'
            }
        }

        stage("Build and Test"){
            steps{
                sh 'ls -ltr'
                 // build the project and create a JAR file
                 sh ' mvn clean package'
            }
        }



    }
}


