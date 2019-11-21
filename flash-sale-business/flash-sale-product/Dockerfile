FROM java:8
MAINTAINER zhenyed
VOLUME /tmp
ADD target/flash-sale-product-1.0.jar app.jar
RUN bash -c "touch /app.jar"
EXPOSE 7011
ENTRYPOINT ["java","-jar","/app.jar"]