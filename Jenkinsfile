pipeline {
    agent any
	tools{
		nodejs '20.11.1'
		git 'Employee Management'
	}
    stages {
	stage('testing'){
		steps{
			echo 'something'
		}
	}

        stage('Checkout') {
            steps {
                git 'https://github.com/Aridoshikaju/Employee-Management-Nginx-Load_balanced.git'
            }
        }
        stage('Install Dependancies') {
            steps {
                dir('App/node_server'){
                    sh 'npm install'
                }
            }
        }
        stage('Run Tests') {
            steps {
                dir('App/node_server'){
                    sh 'npm test'
                }
            }
        }
    }
    post{
        always{
            echo 'Build Completed'
        }
    }
}


