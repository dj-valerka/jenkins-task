def call() {
    def dockerRegistryUrl = env.DOCKER_REGISTRY_URL
    def dockerRepo = env.DOCKER_REPO
    def dockerImageName = env.DOCKER_IMAGE_NAME
    def buildNumber = env.BUILD_NUMBER

    // Build the Docker image with the build number as the tag
    sh """
        docker build -t ${dockerRegistryUrl}/${dockerRepo}/${dockerImageName}:${buildNumber} --no-cache .
    """
    
    // Tag the image as 'latest'
    sh """
        docker tag ${dockerRegistryUrl}/${dockerRepo}/${dockerImageName}:${buildNumber} ${dockerRegistryUrl}/${dockerRepo}/${dockerImageName}:latest
    """
}