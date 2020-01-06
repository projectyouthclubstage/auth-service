FROM arm32v7/maven AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package

FROM arm32v7/openjdk:11.0.3-slim-stretch
MAINTAINER Sascha Deeg <sascha.deeg@gmail.com>
USER root
RUN apt-get update
RUN apt-get -y install curl
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/auth-server-*.jar /root/auth-server.jar
EXPOSE 8080
HEALTHCHECK CMD curl -f http://localhost:8080/actuator/health || exit 1;
CMD java -Dspring.profiles.active=cloud -jar /root/auth-server.jar
