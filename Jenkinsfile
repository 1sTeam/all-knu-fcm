pipeline {
    agent any
    options {
        timeout(time: 1, unit: 'HOURS') // set timeout 1 hour
    }
    environment {
        REPOSITORY_CREDENTIAL_ID = 'all-knu-fcm-jenkins-github-key' // github repository credential name
        REPOSITORY_URL = 'git@github.com:1sTeam/all-knu-fcm.git'
        TARGET_BRANCH = 'main'
        IMAGE_NAME = 'all-knu-fcm'
        CONTAINER_NAME = 'all-knu-fcm'
        PROFILE = 'prod'
	DOCKER_NETWORK = 'all-knu-haproxy-net'
    }
    stages{
        stage('init') {
            steps {
                echo 'init stage'
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
	stage('create secret file firebase sdk by aws parameter store') {
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
    stage('create application-${env.PROFILE} properties by aws parameter store') {
        steps {
    	    dir('src/main/resources') {
    		    withAWSParameterStore(credentialsId: 'aws-all-knu',
                   	path: "/all-knu/properties/${env.PROFILE}",
                   	naming: 'basename',
                   	regionName: 'ap-northeast-2') {
                   	    writeFile file: "application-${env.PROFILE}.yml", text: "${env.FCM}"
                }
    	    }
        }
    	post {
            success {
                echo 'success create secret file'
            }
            failure {
                slackSend (channel: '#jenkins-notification', color: '#FF0000', message: "${env.CONTAINER_NAME} CI / CD 파이프라인 구동 실패, 젠킨스 확인 해주세요")
                error 'fail create secret file'
            }
    	}
    }
    stage('dockerizing by Dockerfile') {
        steps {
            sh '''
            docker build -t $IMAGE_NAME .
            '''
        }
        post {
            success {
               echo 'success dockerizing by Dockerfile'
            }
            failure {
                slackSend (channel: '#jenkins-notification', color: '#FF0000', message: "${env.CONTAINER_NAME} CI / CD 파이프라인 구동 실패, 젠킨스 확인 해주세요")
                error 'fail dockerizing by Dockerfile' // exit pipeline
            }
        }
    }
        stage('rm container') {
                    steps {
                        sh 'docker rm -f $CONTAINER_NAME'
                    }

                    post {
                        success {
                            echo 'success rm container all knu fcm'
                            slackSend (channel: '#jenkins-notification', color: '#00FF00', message: "${env.CONTAINER_NAME} CI / CD 파이프라인 구동 성공")
                        }
                        failure {
                            error 'fail rm container all knu fcm' // exit pipeline
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
