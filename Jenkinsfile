pipeline {
    agent any
    options {
        timeout(time: 1, unit: 'HOURS') // set timeout 1 hour
    }
    environment {
        REPOSITORY_CREDENTIAL_ID = 'all-knu-fcm-jenkins-github-key' // github repository credential name
        REPOSITORY_URL = 'https://github.com/1sTeam/all-knu-fcm.git'
        TARGET_BRANCH = 'main'
        IMAGE_NAME = 'all-knu-fcm'
        CONTAINER_NAME = 'all-knu-fcm'
        PROFILE = 'test'
	DOCKER_NETWORK = 'haproxy-net'
    }
    stages{
        stage('init') {
            steps {
                echo 'init stage'
                sh '''
                docker rm -f $CONTAINER_NAME
                '''
                deleteDir()
            }
            post {
                success {
                    echo 'success init in pipeline'
                }
                failure {
                    error 'error init in pipeline'
                    slackSend (channel: '#jenkins-notification', color: '#FF0000', message: "${env.CONTAINER_NAME} CI / CD 파이프라인 구동 실패, 젠킨스 확인 해주세요")
                }
            }
        }
        stage('clone project') {
            steps {
                git url: "$REPOSITORY_URL",
                    branch: "$TARGET_BRANCH",
                    credentialsId: "$REPOSITORY_CREDENTIAL_ID"
                sh "ls -al"
            }
            post {
                success {
                    echo 'success clone project'
                }
                failure {
                    error 'fail clone project' // exit pipeline
                    slackSend (channel: '#jenkins-notification', color: '#FF0000', message: "${env.CONTAINER_NAME} CI / CD 파이프라인 구동 실패, 젠킨스 확인 해주세요")
                }
            }
        }
	stage('create secret file by aws parameter store') {
		steps {
			dir('src/main/resources/secrets/firebase'){
				withAWSParameterStore(credentialsId: 'aws-all-knu',
               				path: "/all-knu/firebase_${env.PROFILE}",
               				naming: 'basename',
               				regionName: 'ap-northeast-2') {
                                	writeFile file: 'all-knu-firebase-adminsdk.json', text: "${env.ADMINSDK}"
              		 	}
			}
               }
	   post {
		success {
		   echo 'success create secret file'
		}
		failure {
		   error 'fail create secret file'
		   slackSend (channel: '#jenkins-notification', color: '#FF0000', message: "${env.CONTAINER_NAME} CI / CD 파이프라인 구동 실패, 젠킨스 확인 해주세요")
		}
	   }
	}
        stage('dockerizing by maven') {
            steps {
                sh '''
                ./mvnw spring-boot:build-image -DskipTests -Dspring-boot.build-image.imageName=$IMAGE_NAME
                '''
            }
            post {
                success {
                    echo 'success dockerizing by maven'
                }
                failure {
                    error 'fail dockerizing by maven' // exit pipeline
                    slackSend (channel: '#jenkins-notification', color: '#FF0000', message: "${env.CONTAINER_NAME} CI / CD 파이프라인 구동 실패, 젠킨스 확인 해주세요")
                }
            }
        }
        stage('deploy') {
            steps {
                sh 'docker run --name $CONTAINER_NAME -e "SPRING_PROFILES_ACTIVE=$PROFILE" -m="900M" --net $DOCKER_NETWORK -d -t $IMAGE_NAME'
            }

            post {
                success {
                    echo 'success deploying all knu fcm spring project'
                    slackSend (channel: '#jenkins-notification', color: '#00FF00', message: "${env.CONTAINER_NAME} CI / CD 파이프라인 구동 성공")
                }
                failure {
                    error 'fail deploying all knu fcm spring project' // exit pipeline
                    slackSend (channel: '#jenkins-notification', color: '#FF0000', message: "${env.CONTAINER_NAME} CI / CD 파이프라인 구동 실패, 젠킨스 확인 해주세요")
                }
            }
        }

    }

}
