FROM java:8
MAINTAINER zhenyed
VOLUME /tmp
ADD target/flash-sale-user-1.0.jar app.jar
RUN bash -c "touch /app.jar"
EXPOSE 7001
ENTRYPOINT ["java","-jar","/app.jar"]