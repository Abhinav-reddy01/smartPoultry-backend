# Build stage using official Maven with Temurin JDK 17
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copy source code and build executable jar
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage using lightweight Temurin JRE 17
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Create directory for persistent/fallback data if needed
RUN mkdir -p /app/data

# Copy built jar from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENV PORT=8080

# Run with dynamic port binding for Render ($PORT)
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8080}"]
