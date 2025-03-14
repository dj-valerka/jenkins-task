@Library ('jenkins-task-library') _
pipeline {

    agent any
    tools{
        jdk 'openjdk-8'
        maven 'maven3'
    }
    environment {
        ARTIFACTORY_URL = "http://172.23.0.4:8082"
        ARTIFACTORY_REPO = "artifactory"
        DOCKER_REGISTRY_URL = 'localhost:8092'
        DOCKER_REPO = 'docker-local'
        DOCKER_IMAGE_NAME = 'jenkins-task'
    }
    stages{
        stage("Compile"){
            steps{
                compile()
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
                    apt update && apt install -y wget;
                    mkdir -p /usr/share/keyrings;
                    wget -qO - https://releases.jfrog.io/artifactory/api/v2/repositories/jfrog-debs/keyPairs/primary/public | gpg --batch --yes --dearmor -o /usr/share/keyrings/jfrog.gpg
                    echo "deb [signed-by=/usr/share/keyrings/jfrog.gpg] https://releases.jfrog.io/artifactory/jfrog-debs focal contrib" | tee /etc/apt/sources.list.d/jfrog.list
                    apt update;
                    apt install -y jfrog-cli-v2-jf;
                '''
            }
        }
        stage("Publish *.jar to JFrog Artifactory"){
            steps{
                script{
                    withCredentials([usernamePassword(credentialsId: "jfrog-credentials", usernameVariable: "JFROG_USER", passwordVariable: "JFROG_PASSWRORD")]){
                        sh '''
                            jf config add --artifactory-url $ARTIFACTORY_URL/$ARTIFACTORY_REPO --user $JFROG_USER --password $JFROG_PASSWRORD
                            jf rt upload "target/*.jar" maven/
                        '''
                    }
                }
            }
        }
        stage("Build docker image"){
            steps{
                sh "docker build -t ${DOCKER_REGISTRY_URL}/${DOCKER_REPO}/${DOCKER_IMAGE_NAME}:$env.BUILD_NUMBER --no-cache ."
                sh "docker tag ${DOCKER_REGISTRY_URL}/${DOCKER_REPO}/${DOCKER_IMAGE_NAME}:$env.BUILD_NUMBER ${DOCKER_REGISTRY_URL}/${DOCKER_REPO}/${DOCKER_IMAGE_NAME}:latest "
            }
        }
        stage("Publish docker image to JFrog Registry"){
            steps{
                script{
                    withCredentials([usernamePassword(credentialsId: "jfrog-credentials", usernameVariable: "JFROG_USER", passwordVariable: "JFROG_PASSWORD")]){
                        sh ''' 
                           echo $JFROG_PASSWORD|docker login -u admin --password-stdin ${DOCKER_REGISTRY_URL}
                        '''
                        sh "docker push ${DOCKER_REGISTRY_URL}/${DOCKER_REPO}/${DOCKER_IMAGE_NAME}:$env.BUILD_NUMBER"
                        
                    }
                }
            }
        }         
        // stage("Publish docker image"){
        //     steps{
        //         script{
        //             withCredentials([usernamePassword(credentialsId: "docker-credentials", usernameVariable: "DOCKER_REPOSITORY_USER", passwordVariable: "DOCKER_REPOSITORY_PASSWORD")]){
        //                 sh "docker login -u $DOCKER_REPOSITORY_USER -p $DOCKER_REPOSITORY_PASSWORD"
        //                 sh "docker push djvalerka/jenkins:$env.BUILD_NUMBER"
        //             }
        //         }
        //     }
        // }
    }
}
