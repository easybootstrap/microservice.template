FROM clojure:temurin-17-alpine AS builder

RUN mkdir -p /build
WORKDIR /build

# Cache and install Clojure dependencies
# Add before copying code to cache the layer even if code changes
COPY deps.edn Makefile /build/
RUN make deps

# Copy project to working directory
# .dockerignore file excludes all but essential files
COPY ./ /build

# - uses command `clojure -T:build uberjar :uber-file api`
RUN make build-template

# Official OpenJDK Image
FROM eclipse-temurin:17-alpine

LABEL org.opencontainers.image.authors="easybootstrap@matheusfrancisco.com"

# Create Non-root group and user to run service securely
RUN addgroup -S admin && adduser -S admin -G admin 

# Create directory to contain service archive, owned by non-root user
RUN mkdir -p /service && chown -R admin. /service

# Tell docker that all future commands should run as the appuser user
USER admin

# Copy service archive file from Builder image
WORKDIR /service
COPY --from=builder /build/target/service.jar /service/service.jar

# ------------------------
# Set Service Environment variables

# optional over-rides for Integrant configuration
# ENV HTTP_SERVER_PORT=
# ENV MYSQL_DATABASE=
ENV PROFILE=prod

# Expose port of HTTP Server
EXPOSE 3000

# ------------------------
# Run service

# JDK_JAVA_OPTIONS environment variable for setting JVM options
# Use JVM options that optomise running in a container
# For very low latency, use the Z Garbage collector "-XX:+UseZGC"
ENV JDK_JAVA_OPTIONS "-XshowSettings:system -XX:+UseContainerSupport -XX:MaxRAMPercentage=90"

# (overrides `jshell` entrypoint - default in eclipse-temuring image)
ENTRYPOINT ["java", "-jar", "/service/service.jar"]
