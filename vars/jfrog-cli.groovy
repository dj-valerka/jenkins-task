def call () {
    sh """
        apt update && apt install -y wget;
        mkdir -p /usr/share/keyrings;
        wget -qO - https://releases.jfrog.io/artifactory/api/v2/repositories/jfrog-debs/keyPairs/primary/public | gpg --batch --yes --dearmor -o /usr/share/keyrings/jfrog.gpg
        echo "deb [signed-by=/usr/share/keyrings/jfrog.gpg] https://releases.jfrog.io/artifactory/jfrog-debs focal contrib" | tee /etc/apt/sources.list.d/jfrog.list
        apt update;
        apt install -y jfrog-cli-v2-jf;
    """
}