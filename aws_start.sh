#!/bin/bash

GOOGLE_APPLICATION_CREDENTIALS=/home/ec2-user/Dialogflow/DialogflowSpringProject/banking-dggfcs-8aad97663931.json

export GOOGLE_APPLICATION_CREDENTIALS	

echo $GOOGLE_APPLICATION_CREDENTIALS






JAVA_HOME=/home/ec2-user/java/jdk-11.0.6
PATH=/home/ec2-user/java/jdk-11.0.6/bin:$PATH

M2_HOME=/usr/local/src/apache-maven
MAVEN_HOME=/usr/local/src/apache-maven
PATH=${M2_HOME}/bin:${PATH}

mvn clean install -DskipTests
java -Xdebug -Xrunjdwp:transport=dt_socket,address=5670,server=y,suspend=n -jar target/DialogflowSpringProject-0.0.1-SNAPSHOT.jar dialogflow
