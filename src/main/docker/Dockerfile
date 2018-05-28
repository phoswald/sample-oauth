FROM openjdk:8-jre-alpine
COPY maven/oauth-sample-service.jar /usr/local/application/
COPY maven/lib                      /usr/local/application/lib
EXPOSE 8080
WORKDIR /usr/local/application
CMD exec java -cp "oauth-sample-service.jar:lib/*" com.github.phoswald.oauth.sample.application.Main
