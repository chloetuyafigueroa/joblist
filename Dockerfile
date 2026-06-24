FROM eclipse-temurin:8-jdk AS build

RUN apt-get update && apt-get install -y ant && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY . .

RUN ant war
RUN jar tf /app/dist/Joblist.war > /tmp/war_check.txt

FROM tomcat:9-jdk17

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /app/dist/Joblist.war /usr/local/tomcat/webapps/Joblist.war

EXPOSE 8080

CMD ["catalina.sh", "run"]