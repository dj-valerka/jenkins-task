def call(String credentialsId) {
    def dockerRegistryUrl = env.DOCKER_REGISTRY_URL
    def dockerRepo = env.DOCKER_REPO
    def dockerImageName = env.DOCKER_IMAGE_NAME
    def buildNumber = env.BUILD_NUMBER  
    
    // Login to JFrog Artifactory (or other Docker registry)
    withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'JFROG_USER', passwordVariable: 'JFROG_PASSWORD')]) {
        // Login to Docker registry using credentials
        sh """
            echo $JFROG_PASSWORD | docker login -u $JFROG_USER --password-stdin $dockerRegistryUrl
        """
        
        // Push the Docker image to the registry
        sh """
            docker push ${dockerRegistryUrl}/${dockerRepo}/${dockerImageName}:${buildNumber}
        """
    }
}