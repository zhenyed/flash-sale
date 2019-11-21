FROM java:8
MAINTAINER zhenyed
# 这里的 /tmp 目录就会在运行时自动挂载为匿名卷，任何向 /tmp 中写入的信息都不会记录进容器存储层
VOLUME /tmp
ADD target/flash-sale-gateway-1.0.jar app.jar
RUN bash -c "touch /app.jar"
EXPOSE 8001
ENTRYPOINT ["java","-jar","/app.jar"]