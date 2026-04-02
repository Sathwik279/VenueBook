# 🐳 Optimized Multi-Stage Dockerfile (Spring Boot + Java 21)
# --------------------------------------------------------

# Stage 1: Build the Application
# Using Eclipse Temurin (JDK) for high-performance builds
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# [1] CACHING: Copy pom.xml first to cache dependencies (speeds up future builds)
COPY pom.xml .
RUN mvn dependency:go-offline

# [2] BUILD: Copy source code and build the final JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Production-Ready Runtime
# Using a slim JRE (Java Runtime Environment) to keep the image small
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# // UPDATE THIS: Ensure the JAR filename matches your build output (usually artifact-id-version.jar)
# Most Spring Boot projects output to /app/target/*.jar
COPY --from=build /app/target/*.jar app.jar

# [3] DATABASE SECURITY: Download & Convert CA certificate for TiDB Cloud
# JDBC requires JKS or PKCS12 format, we convert the PEM text file using keytool.
RUN mkdir -p /app/certs && \
    apt-get update && apt-get install -y curl && \
    curl -o /app/certs/isrgrootx1.pem https://letsencrypt.org/certs/isrgrootx1.pem.txt && \
    keytool -importcert -alias tidb -file /app/certs/isrgrootx1.pem -keystore /app/certs/truststore.jks -storepass changeit -noprompt && \
    apt-get clean && rm -rf /var/lib/apt/lists/*


# Set default configuration (can be overridden in Cloud Run)
ENV SPRING_PROFILES_ACTIVE=prod

# Expose standard Spring Boot port
EXPOSE 8080

# Execute the application
# Use the jar name defined in the COPY step above
ENTRYPOINT ["java", "-jar", "app.jar"]
