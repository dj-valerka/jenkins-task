pipeline {

    agent any
    tools{
        jdk 'openjdk-8'
        maven 'maven3'
    }
    environment {
    // Define variables
        DOCKER_REGISTRY_URL = '172.23.0.2:8092'
        DOCKER_REPO = 'docker-local'
        DOCKER_IMAGE_NAME = 'jenkins-task'
    }
    stages{
        // stage("Compile"){
        //     steps{
        //         sh "mvn clean compile"
        //     }
        // }

        //  stage("Test Cases"){
        //     steps{
        //         sh "mvn test"
        //     }
        // }

        //  stage("Build"){
        //     steps{
        //         sh "mvn clean install"
        //     }
        // }
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
        // stage("Publish *.jar to JFrog Artifactory"){
        //     steps{
        //         script{
        //             withCredentials([usernamePassword(credentialsId: "jfrog-credentials", usernameVariable: "JFROG_USER", passwordVariable: "JFROG_PASSWRORD")]){
        //                 sh '''
        //                     jf config add --artifactory-url http://172.23.0.4:8082/artifactory --user $JFROG_USER --password $JFROG_PASSWRORD
        //                     jf rt upload "target/*.jar" maven/
        //                 '''
        //             }
        //         }
        //     }
        // }
        // stage("Build docker image"){
        //     steps{
        //         sh "docker build -t ${DOCKER_REGISTRY_URL}/${DOCKER_REPO}/${DOCKER_IMAGE_NAME}:$env.BUILD_NUMBER --no-cache ."
        //         sh "docker tag ${DOCKER_REGISTRY_URL}/${DOCKER_REPO}/${DOCKER_IMAGE_NAME}:$env.BUILD_NUMBER ${DOCKER_REGISTRY_URL}/${DOCKER_REPO}/${DOCKER_IMAGE_NAME}:latest "
        //     }
        // }
        stage("Publish docker image to JFrog Registry"){
            steps{
                script{
                    withCredentials([usernamePassword(credentialsId: "jfrog-credentials", usernameVariable: "JFROG_USER", passwordVariable: "JFROG_PASSWORD")]){
                        sh ''' 
                            docker login ${DOCKER_REGISTRY_URL} -u admin -p ${JFROG_PASSWRORD}
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
