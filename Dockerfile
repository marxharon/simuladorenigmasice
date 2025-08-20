# Etapa 1: Build com Maven e JDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copia apenas o pom.xml e baixa dependências (cache eficiente)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o restante do código e compila
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final só com JDK
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copia o artefato gerado (JAR ou WAR)
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Comando para rodar o app
CMD ["java", "-jar", "app.jar"]
