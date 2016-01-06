[![Build Status](https://travis-ci.org/jcooky/jandy.svg?branch=master)](https://travis-ci.org/jcooky/jandy)

# Jandy 
Jandy is a dynamic analysis project for opensource developer using on github and travis-ci

# Abstract
This project is made for opensource developers. The purpose of this project is analysis of performance and finds a tunning point of your applications. To open source developer who want to improve performance of your application, he and she should be used to find performance improvement on this service by application profiling. Hence, it support for many languages(java, python, ruby, etc…), then our service focused language specific performance(multi threaded, bad syntax uses, …). Then, we show the profile result how calling methods through intuitive, intelligent and efficiently data visualization.

# Key Features
* Automatically testing and profiling application through Travis-CI and Github integration
* Support for major language to java, python, ruby
* Intuitive, Effective and Detailed application profling visualization
* Reporting: Provides to report by E-Mail, Twitter

# How to build
## Prebuild Specification
* JDK 1.8
* Maven
* Thrift command line program 0.9.2
* Python 2.7

## Build
* #mvn -DskipTests=true clean package
* The greem-server is packaged named by “greem-server-#VERSION.jar” in jandy-server/target location
* Execution method: “java -jar greem-server-#VERSION.jar”
* And the agent is made by python and provide the profiler naming of the “jandy-java-profiler”
* A java profiler Installation method is used “-javaagent” option in java command line execution
  * ex) https://github.com/jcooky/jasypt-test
