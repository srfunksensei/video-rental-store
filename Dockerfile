FROM amazoncorretto:8
LABEL maintainer="sr.funk.sensei@gmail.com"
COPY target/video-rental-store-0.0.1-SNAPSHOT.jar video-rental-store.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/video-rental-store.jar"]
