FROM openjdk:17-jdk-slim
VOLUME /tmp
RUN apt-get update && apt-get install -y iputils-ping
RUN apt-get install -y vim
RUN apt-get install -y telnet
ADD target/remittance-processing-service.jar remittance-processing-service.jar
COPY opentelemetry-javaagent-all.jar opentelemetry-javaagent-all.jar
ENTRYPOINT ["java", "-Duser.timezone=Asia/Dhaka", "-javaagent:/opentelemetry-javaagent-all.jar","-jar", "/remittance-processing-service.jar"]
