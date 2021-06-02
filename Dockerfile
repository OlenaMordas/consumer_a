FROM openjdk:11

COPY target/consumer_a-0.0.1-SNAPSHOT.jar consumer_a-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/consumer_a-0.0.1-SNAPSHOT.jar"]