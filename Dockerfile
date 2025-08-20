# ----------------------
# Etapa 1: Build com Maven
# ----------------------
FROM maven:3.9.8-eclipse-temurin-17 AS builder

# Define o diretório de trabalho
WORKDIR /app

# Copia os arquivos de configuração do Maven primeiro (cache das dependências)
COPY pom.xml .
COPY src ./src

# Roda o build e gera o WAR (pula os testes para acelerar)
RUN mvn clean package -DskipTests

# ----------------------
# Etapa 2: Tomcat para rodar o WAR
# ----------------------
FROM tomcat:9.0-jdk17-temurin

# Remove os exemplos padrão do Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia o WAR gerado na etapa de build e renomeia para ROOT.war
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Expõe a porta que o Railway vai usar
EXPOSE 8080

# Inicia o Tomcat
CMD ["catalina.sh", "run"]
