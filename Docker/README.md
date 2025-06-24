# Dockerizing the Build_Hello Spring Boot API

This guide explains how to containerize the Spring Boot application using Docker, creating a portable and scalable deployment.

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Docker Setup](#docker-setup)
3. [Building the Docker Image](#building-the-docker-image)
4. [Running the Container](#running-the-container)
5. [Docker Compose](#docker-compose)
6. [Testing the Dockerized API](#testing-the-dockerized-api)
7. [Best Practices](#best-practices)
8. [Troubleshooting](#troubleshooting)

## Prerequisites

Before proceeding, ensure you have:
- Docker installed on your system
- The Spring Boot application built (using `mvn clean install`)
- Basic understanding of Docker concepts

## Docker Setup

### 1. Create a Dockerfile

Create a file named `Dockerfile` in your project root with the following content:

```dockerfile
# Multi-stage build for smaller final image
# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable and download dependencies
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy only the JAR file from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Create non-root user for security
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Run the application
CMD ["java", "-jar", "app.jar"]
```

### 2. Create a .dockerignore file

Create a `.dockerignore` file to exclude unnecessary files from the Docker build context:

```
.git
.gitignore
target/
.mvn/
.mvnw
*.iml
.idea/
*.md
Dockerfile
docker-compose.yml
```

## Building the Docker Image

Run the following command to build your Docker image:

```bash
docker build -t build-hello-api .
```

This will:
1. Use a multi-stage build to keep the final image small
2. Cache Maven dependencies for faster rebuilds
3. Create an optimized production-ready image

## Running the Container

To run your containerized application:

```bash
docker run -d -p 8080:8080 --name hello-api build-hello-api
```

Options:
- `-d`: Run in detached mode
- `-p 8080:8080`: Map host port 8080 to container port 8080
- `--name`: Give your container a name

Verify it's running:
```bash
docker ps
```


## Testing the Dockerized API

Once running, test the API endpoints as before, but now it's running in a container:

```bash
curl http://localhost:8080/hello
```

Or test the health endpoint:
```bash
curl http://localhost:8080/actuator/health
```

## Best Practices

1. **Multi-stage builds**: Keep production images small
2. **Non-root user**: Add this to your Dockerfile for security:
   ```dockerfile
   RUN adduser --system --group spring
   USER spring:spring
   ```
3. **Resource limits**: Set memory limits when running:
   ```bash
   docker run -d -p 8080:8080 --memory=512m --cpus=1 --name hello-api build-hello-api
   ```
4. **Environment variables**: Externalize configuration:
   ```dockerfile
   ENV SPRING_PROFILES_ACTIVE=prod
   ```
5. **Image scanning**: Regularly scan for vulnerabilities:
   ```bash
   docker scan build-hello-api
   ```

## Troubleshooting

1. **Build fails**:
    - Check Docker has enough resources (memory/CPU)
    - Run with `--no-cache` to force a fresh build:
      ```bash
      docker build --no-cache -t build-hello-api .
      ```

2. **Container exits immediately**:
    - Check logs:
      ```bash
      docker logs hello-api
      ```
    - Run interactively to see output:
      ```bash
      docker run -it --rm -p 8080:8080 build-hello-api
      ```

3. **Port conflicts**:
    - Change the host port mapping:
      ```bash
      docker run -d -p 9090:8080 --name hello-api build-hello-api
      ```

4. **Connection refused**:
    - Verify container is running:
      ```bash
      docker ps
      ```

