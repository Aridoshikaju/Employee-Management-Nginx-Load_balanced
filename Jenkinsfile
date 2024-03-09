pipeline {
    agent any
    
    tools {
        nodejs '20.11.1'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/Aridoshikaju/Employee-Management-Nginx-Load_balanced.git'
            }
        }
        stage('Install Dependencies') {
            steps {
                dir('App/node_server') {
                    sh 'npm install'
                }
            }
        }
        stage('Run Tests') {
            steps {
                script {
                    try {
                        dir('App/node_server') {
                            sh 'npm test'
                        }
                    } catch (err) {
                        currentBuild.result = 'FAILURE'
                        error("Tests failed: ${err}")
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo 'Build Completed'
        }
        failure {
            echo 'Build failed'
            // Notify team or take other actions here
        }
    }
}
