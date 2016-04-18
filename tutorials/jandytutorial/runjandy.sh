#!/bin/bash

# git clone https://github.com/jcooky/jandy.git
# cd ./jandy
cd ../..
mvn -DskipTests=true clean package
# cd ./jandy-server/target

jar_file_name=`ls ./jandy-server/target | grep '^jandy-server.*jar$'`
# echo $jar_file_name

# java -jar ./jandy-server/target/jandy-server-0.4.0-SNAPSHOT.jar multipart.max-file-size 512Mb multipart.max-request-size -1 &
java -jar ./jandy-server/target/$jar_file_name multipart.max-file-size 512Mb multipart.max-request-size -1 &
