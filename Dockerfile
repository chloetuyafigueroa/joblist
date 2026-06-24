FROM tomcat:9-jdk17

COPY Joblist.war /usr/local/tomcat/webapps/Joblist.war

EXPOSE 8080

CMD ["catalina.sh", "run"]