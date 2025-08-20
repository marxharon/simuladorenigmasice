# Etapa 1: Build com Maven + JDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copia pom.xml e baixa dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia código fonte e compila
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Usar Tomcat para rodar o WAR
FROM tomcat:9.0-jdk17

# Remove os apps padrão do Tomcat (opcional, para evitar o ROOT default)
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia o WAR gerado para o Tomcat
COPY --from=build /target/*.war /usr/local/tomcat/webapps/ROOT.war

# Expor porta padrão do Tomcat
EXPOSE 8080

# Comando padrão do Tomcat
CMD ["catalina.sh", "run"]
