pipeline {
    agent any
    tools{
        jdk 'openjdk-8'
        maven 'maven3'
    }

    stages{
        stage("Compile"){
            steps{
                sh "mvn clean compile"
            }
        }

         stage("Test Cases"){
            steps{
                sh "mvn test"
            }
        }

         stage("Build"){
            steps{
                sh "mvn clean install"
            }
        }
        stage('Install JFrog CLI'){
            steps{
                sh '''
                    sudo mkdir -p /usr/share/keyrings;
                    wget -qO - https://releases.jfrog.io/artifactory/api/v2/repositories/jfrog-debs/keyPairs/primary/public | sudo gpg --dearmor -o /usr/share/keyrings/jfrog.gpg
                    echo "deb [signed-by=/usr/share/keyrings/jfrog.gpg] https://releases.jfrog.io/artifactory/jfrog-debs focal contrib" | sudo tee /etc/apt/sources.list.d/jfrog.list
                    sudo apt update;
                    sudo apt install -y jfrog-cli-v2-jf;
                '''
            }
        }
        stage("Publish *.jar to JFrog Artifactory"){
            steps{
                sh "jf --version"
            }
        }

        stage("Build docker image"){
            steps{
                sh "docker build -t djvalerka/jenkins:$env.BUILD_NUMBER ."
            }
        }
        stage("Publish docker image"){
            steps{
                script{
                    withCredentials([usernamePassword(credentialsId: "docker-credentials", usernameVariable: "DOCKER_REPOSITORY_USER", passwordVariable: "DOCKER_REPOSITORY_PASSWORD")]){
                        sh "docker login -u $DOCKER_REPOSITORY_USER -p $DOCKER_REPOSITORY_PASSWORD"
                        sh "docker push djvalerka/jenkins:$env.BUILD_NUMBER"
                    }
                }
            }
        }
    }
}
