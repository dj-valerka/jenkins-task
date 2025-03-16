def call(Map config = [:]) {
    def dockerRegistryUrl = config.get('dockerRegistryUrl', '')
    def dockerRepo = config.get('dockerRepo', '')
    def dockerImageName = config.get('dockerImageName', '')
    def buildNumber = env.BUILD_NUMBER
    
    // Use withCredentials to inject the JFrog credentials
    withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'JFROG_USER', passwordVariable: 'JFROG_PASSWORD')]) {
        
        // Log in to JFrog Artifactory (or another Docker registry)
        sh """
            echo $JFROG_PASSWORD | docker login -u $JFROG_USER --password-stdin $dockerRegistryUrl
        """
        
        // Push the Docker image to the registry with the BUILD_NUMBER tag
        sh """
            docker push $dockerRegistryUrl/$dockerRepo/$dockerImageName:$buildNumber
        """
    }
}
