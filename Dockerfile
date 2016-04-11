FROM java:8
RUN ["mkdir", "/jandy"]
COPY ./jandy-server/target/jandy-server-*.jar /jandy/jandy-server.jar
WORKDIR /jandy
CMD ["java", "-jar", "jandy-server.jar"]
