FROM openjdk:8-alpine

COPY target/uberjar/potato.jar /potato/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/potato/app.jar"]
