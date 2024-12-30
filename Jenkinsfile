pipeline{
    agent{
        docker {image 'maven:3.8.1-adoptopenjdk-11'}
    }
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


