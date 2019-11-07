pipeline {
    agent {
      label "jenkins-maven"
    }
    environment {
      ORG               = 'cloudpocstation'
      APP_NAME          = 'micro'
      CHARTMUSEUM_CREDS = credentials('jenkins-x-chartmuseum')
    }
    stages {
        stage('CI: Static Code Analysis') {
            sh "echo call sonar here"
        }
      stage('CI: Build') {
      when { anyOf { branch 'master'; branch 'PR-*' } }
        environment {
          PREVIEW_VERSION = "0.0.0-SNAPSHOT-$BRANCH_NAME-$BUILD_NUMBER"
          PREVIEW_NAMESPACE = "$APP_NAME-$BRANCH_NAME".toLowerCase()
          HELM_RELEASE = "$PREVIEW_NAMESPACE".toLowerCase()
        }
        steps {
          container('maven') {
            sh "mvn versions:set -DnewVersion=$PREVIEW_VERSION"
            sh "mvn install"
          }
        }
      }
      stage('CI: Push Snapshot') {
            when { anyOf { branch 'master'; branch 'PR-*' } }
               environment {
                  PREVIEW_VERSION = "0.0.0-SNAPSHOT-$BRANCH_NAME-$BUILD_NUMBER"
               }
              steps {
                  container('maven') {
                      sh 'export VERSION=$PREVIEW_VERSION && skaffold build -f skaffold.yaml'
                      sh "jx step post build --image $DOCKER_REGISTRY/$ORG/$APP_NAME:$PREVIEW_VERSION"
                  }
              }
            }
      stage('TEST: Create Preview Environment') {
      when { anyOf { branch 'PR-*' } }
        environment {
          PREVIEW_NAMESPACE = "$APP_NAME-$BRANCH_NAME".toLowerCase()
          PREVIEW_VERSION = "0.0.0-SNAPSHOT-$BRANCH_NAME-$BUILD_NUMBER"
          HELM_RELEASE = "$PREVIEW_NAMESPACE".toLowerCase()
        }
        steps {
        dir ('./charts/preview') {
         container('maven') {
           sh "make preview"
           sh "jx preview --name=${ORG}-${APP_NAME}-${BRANCH_NAME} --verbose=true --app $APP_NAME --dir ../.."
         }
        }
        }
      }
      stage('TEST: Smoke Tests') {
      when { anyOf { branch 'PR-*' } }
        environment {
          PREVIEW_NAMESPACE = "$APP_NAME-$BRANCH_NAME".toLowerCase()
          PREVIEW_URL = "http://micro.jx-cloudpocstation-${PREVIEW_NAMESPACE}.cloud-poc-station.com"
          RESOURCE_URL = "${PREVIEW_URL}/micro-sample/rs/monitoring/ping"
        }
        steps {
        dir ('./test') {
          container('maven') {
            sh "make wait-for-resource"
            sh "make test"
          }
        }
        }
      }
      stage('TEST: Module and subsystem tests') {
      when { anyOf { branch 'PR-*' } }
              environment {
                PREVIEW_NAMESPACE = "$APP_NAME-$BRANCH_NAME".toLowerCase()
                PREVIEW_URL = "http://micro.jx-cloudpocstation-${PREVIEW_NAMESPACE}.cloud-poc-station.com"
                RESOURCE_URL = "${PREVIEW_URL}/micro-sample/rs/monitoring/ping"
              }
              steps {
              dir ('./test') {
                container('maven') {
                  sh "make wait-for-resource"
                  sh "make it-test"
                }
              }
              }
            }
      stage('Build Release') {
        when {
          branch 'master'
        }
        steps {
          container('maven') {
            // ensure we're not on a detached head
            sh "git checkout master"
            sh "git config --global credential.helper store"

            sh "jx step git credentials"
            // so we can retrieve the version in later steps
            sh "echo \$(jx-release-version) > VERSION"
            sh "mvn versions:set -DnewVersion=\$(cat VERSION)"
          }
          dir ('./charts/micro') {
            container('maven') {
              sh "make tag"
            }
          }
          container('maven') {
            // sh 'mvn clean deploy'
            sh 'export VERSION=`cat VERSION` && skaffold build -f skaffold.yaml'
            sh "jx step post build --image $DOCKER_REGISTRY/$ORG/$APP_NAME:\$(cat VERSION)"
          }
        }
      }
      stage('Promote to Environments') {
        when {
          branch 'master'
        }
        steps {
          dir ('./charts/micro') {
            container('maven') {
              sh 'jx step changelog --version v\$(cat ../../VERSION)'

              // release the helm chart
              sh 'jx step helm release'

              // promote through all 'Auto' promotion Environments
              sh 'jx promote -b --all-auto --timeout 1h --version \$(cat ../../VERSION)'
            }
          }
        }
      }
    }
    post {
        always {
            cleanWs()
        }
        failure {
            input """Pipeline failed. 
We will keep the build pod around to help you diagnose any failures. 

Select Proceed or Abort to terminate the build pod"""
        }
    }
  }
