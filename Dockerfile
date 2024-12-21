# 基于官方OpenJDK镜像
FROM openjdk:8

# 指定维护者信息
LABEL maintainer="mydoctorhost@gmail.com"

# 在镜像中创建一个目录存放我们的应用
VOLUME /tmp

# 将jar文件添加到容器中并更名为app.jar
ADD ruoyi-admin/target/ruoyi-admin.jar app.jar
ADD ruoyi-admin/target/classes/application-prod.yml application-prod.yml

# 暴露容器内的8080端口
EXPOSE 8080

# 定义环境变量
ENV JAVA_OPTS=""

# 在容器启动时运行jar包
ENTRYPOINT exec java $JAVA_OPTS  -jar /app.jar --spring.config.import=file:/application-prod.yml
