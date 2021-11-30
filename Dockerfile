FROM openjdk:11

WORKDIR /app

COPY target/*.jar /app/carteira-api.jar

EXPOSE 8089

CMD java -XX:+UseContainerSupport -jar carteira-api.jar
