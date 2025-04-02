FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/Teste-Pratico-Desenvolvedor-Java-0.0.2-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]