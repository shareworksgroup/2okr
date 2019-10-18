FROM frolvlad/alpine-oraclejre8
VOLUME /tmp
ADD ./target/2okr-web-api-1.0.0.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
