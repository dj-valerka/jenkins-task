def call(Map config = [:]) {
    def artifactoryUrl = config.get('artifactoryUrl', '')
    def artifactoryRepo = config.get('artifactoryRepo', '')
    def credentialsId = config.get('credentialsId', '')
    
    // Publish jar to Artifactory
    withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'JFROG_USER', passwordVariable: 'JFROG_PASSWORD')]) {
        sh """
            jf config add --artifactory-url $artifactoryUrl/$artifactoryRepo --user $JFROG_USER --password $JFROG_PASSWORD
            jf rt upload "target/*.jar" maven/
        """
    }
}