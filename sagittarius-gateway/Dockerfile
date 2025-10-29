# 使用官方 GraalVM JDK 镜像作为基础镜像
FROM ghcr.io/graalvm/jdk:ol8-java17-22.3.3

# 维护者信息
MAINTAINER lazzz zydot998@gmail.com

# 创建临时卷，用于存放临时文件
VOLUME /tmp

# 将构建生成的 JAR 文件复制到容器中并重命名为 /app.jar
COPY build/libs/sagittarius-gateway.jar /app.jar

# 设置暴露端口
EXPOSE 8800

ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

# 容器启动时执行的命令
# -Xms 和 -Xmx 设置最小和最大堆内存为 128MB
# -Djava.security.egd=file:/dev/./urandom 提高随机数生成速度
CMD ["java", \
     "-Xms128m", \
     "-Xmx128m", \
     "-Djava.security.egd=file:/dev/./urandom", \
     "-jar", \
     "/app.jar"]
