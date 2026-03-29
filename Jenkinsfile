pipeline {
    agent any

    environment {
        DOCKER_USER = "dockervibh"
        APP_NAME = "vibh-app"
        // Stable Tagging: Using Build Number instead of :latest
        IMAGE_TAG = "v${env.BUILD_NUMBER}"
        DOCKER_HUB_IMAGE = "${DOCKER_USER}/practice_java:${IMAGE_TAG}"
    }

    tools {
        jdk 'jdk_17'
        maven 'mvn'
    }

    stages {
        stage('Initialize & Cache') {
            steps {
                // Optimization: Tell Maven to use a local repository within the workspace 
                // so Jenkins can cache it between builds.
                sh 'mvn dependency:go-offline -B'
            }
        }

        stage('Build & Test') {
            steps {
                // Optimization: Combined Build and Test with Parallel Threading (-T 1C)
                // Also skipping 'clean' unless necessary to save time.
                sh 'mvn -T 1C package -B'
            }
            post {
                success {
                    // Optimization: Archive the JAR so you have a backup outside of Docker
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                // Using the stable tag defined in environment
                sh "docker build -t ${DOCKER_HUB_IMAGE} ."
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([string(credentialsId: 'docker_PAT', variable: 'DOCKER_TOKEN')]) {
                    sh """
                        echo "$DOCKER_TOKEN" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${DOCKER_HUB_IMAGE}
                        docker logout
                    """
                }
            }
        }
    }
}