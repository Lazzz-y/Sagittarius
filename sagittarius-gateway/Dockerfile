# 基础镜像：保留 GraalVM 官方镜像，确保运行环境兼容
FROM ghcr.io/graalvm/jdk:ol8-java17-22.3.3 AS builder-stage

# 维护者信息（推荐用 LABEL 替代 MAINTAINER，更灵活支持元数据扩展）
LABEL maintainer="Zayn <zydot998@gmail.com>" \
      description="Sagittarius Gateway 网关镜像"

# 合并 ENV 指令：减少镜像层数，降低缓存冗余
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8

# -----------------------------------------------------------------------------
# 构建阶段：仅复制 JAR 包（无额外构建步骤，直接复用本地构建好的 JAR）
# 若后续需要编译原生镜像（GraalVM 特性），可在此阶段添加 native-image 构建逻辑
# -----------------------------------------------------------------------------
COPY build/libs/sagittarius-gateway.jar /app.jar

# -----------------------------------------------------------------------------
# 最终镜像阶段：复用 builder-stage 的文件，进一步精简（多阶段构建减少冗余）
# 注：若无需原生编译，此阶段可简化，但多阶段能确保仅保留必要文件
# -----------------------------------------------------------------------------
FROM ghcr.io/graalvm/jdk:ol8-java17-22.3.3

# 从构建阶段复制 JAR 包（确保文件完整性，避免重复复制的缓存问题）
COPY --from=builder-stage /app.jar /app.jar

# 临时卷：存放运行时临时文件（与原逻辑一致）
VOLUME /tmp

# 暴露端口（仅声明，不自动映射，运行时需手动指定 -p）
EXPOSE 8800

# 环境变量：复用构建阶段的编码配置（避免重复定义）
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8

# 启动命令（保留原 JVM 优化参数，确保运行性能）
CMD ["java", "-jar", "/app.jar"]