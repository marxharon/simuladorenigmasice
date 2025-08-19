# Use OpenJDK como base
FROM openjdk:17

# Diretório de trabalho
WORKDIR /app

# Copia arquivos Maven
COPY pom.xml .
COPY src ./src

# Instala Maven
RUN apt-get update && apt-get install -y maven

# Build da aplicação
RUN mvn clean package

# Expõe a porta do seu app
EXPOSE 8080

# Comando para rodar o WAR
CMD ["java", "-jar", "target/enigma-simulator-1.0.0.war"]
