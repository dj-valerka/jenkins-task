def call(String credentialsId) {
    def dockerRegistryUrl = env.DOCKER_REGISTRY_URL
    def dockerRepo = env.DOCKER_REPO
    def dockerImageName = env.DOCKER_IMAGE_NAME
    def buildNumber = env.BUILD_NUMBER 
    
    withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'JFROG_USER', passwordVariable: 'JFROG_PASSWORD')]) {
        
        // Log in to JFrog Artifactory (or another Docker registry)
        sh """
            echo \$JFROG_PASSWORD | docker login -u \$JFROG_USER --password-stdin \$dockerRegistryUrl
        """
        
        // Push the Docker image to the registry with the BUILD_NUMBER tag
        sh """
            docker push \$dockerRegistryUrl/\$dockerRepo/\$dockerImageName:\$buildNumber
        """
    }
}
