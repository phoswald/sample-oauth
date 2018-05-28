#!/bin/bash
export MY_CALLER="Started from run.sh"
CLASSPATH="target/oauth-sample-service.jar:target/lib/*"
MAINCLASS=com.github.phoswald.oauth.sample.application.Main
java -cp $CLASSPATH $MAINCLASS "$@"
