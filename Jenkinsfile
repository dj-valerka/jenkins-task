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
                jfrogCli()
            }
        }
        stage("Publish *.jar to JFrog Artifactory"){
            steps{
                script{
                    publishJarToArtifactory(
                        artifactoryUrl: '$ARTIFACTORY_URL',
                        artifactoryRepo: '$ARTIFACTORY_REPO',
                        credentialsId: 'jfrog-credentials'
                    )
                }
            }
        }
        stage("Build docker image"){
            steps{
                buildDockerImage()            
            }
        }
        stage("Publish docker image to JFrog Registry"){
            steps{
                script{
                    publishDockerImageToJFrog(
                        dockerRegistryUrl: '$DOCKER_REGISTRY_URL',
                        dockerRepo: '$DOCKER_REPO',
                        dockerImageName: '$DOCKER_IMAGE_NAME',
                        credentialsId: 'jfrog-credentials'
                    )
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
