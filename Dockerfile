FROM openjdk:22-slim
RUN mkdir /work
VOLUME /work
COPY build/libs/*.jar /work/app.jar
WORKDIR /work
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Xmx512m","-jar","/work/app.jar","--help"]
