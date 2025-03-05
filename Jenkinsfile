pipeline {
    agent any
    tools{
        jdk 'openjdk-8'
        maven 'maven3'
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
                            jf config add --artifactory-url http://192.168.1.3:8082/artifactory --user $JFROG_USER --password $JFROG_PASSWRORD
                            jf rt upload "target/*.jar" maven-repo/
                        '''
                    }
                }
            }
        }
        stage("Build docker image"){
            steps{
                sh "docker build -t jenkins:$env.BUILD_NUMBER ."
                sh "docker tag jenkins:$env.BUILD_NUMBER 192.168.1.3:8082/artifactory/docker-repo/jenkins:$env.BUILD_NUMBER"
            }
        }
        stage("Publish docker image to JFrog Artifactory"){
            steps{
                script{
                    withCredentials([usernamePassword(credentialsId: "jfrog-credentials", usernameVariable: "JFROG_USER", passwordVariable: "JFROG_PASSWRORD")]){
                        sh "docker push 192.168.1.3:8082/artifactory/docker-repo/jenkins:$env.BUILD_NUMBER"
                        
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
