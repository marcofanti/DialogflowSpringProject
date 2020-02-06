#!/bin/bash

mvn clean install -DskipTests
java -Xdebug -Xrunjdwp:transport=dt_socket,address=5670,server=y,suspend=n -jar target/login-0.0.1-SNAPSHOT.jar
