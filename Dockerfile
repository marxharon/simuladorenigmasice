FROM tomcat:9.0-jdk17-temurin
COPY target/simuladorenigma-0.7.war /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]
