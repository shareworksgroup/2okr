FROM frolvlad/alpine-oraclejre8
VOLUME /tmp
ADD ./target/okr-1.0.0.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
