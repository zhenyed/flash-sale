FROM java:8
MAINTAINER zhenyed
VOLUME /tmp
ADD target/flash-sale-order-1.0.jar app.jar
RUN bash -c "touch /app.jar"
EXPOSE 7021
ENTRYPOINT ["java","-jar","/app.jar"]