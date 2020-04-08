FROM openjdk:11-jre-slim
LABEL maintainer="szk" 

COPY target/*.jar /app/app.jar
WORKDIR /app/
RUN mkdir /app/config /app/log 
CMD ["java", "-jar", "app.jar"]
