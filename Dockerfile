# Stage 1: Build the application
FROM maven:3.9.4-eclipse-temurin-17 AS builder
LABEL authors="Ayesh"

# Set the working directory inside the container
WORKDIR /app

# Copy only the Maven descriptor files (pom.xml and others)
COPY pom.xml ./

# Download Maven dependencies (caches dependencies if pom.xml doesn't change)
RUN mvn dependency:resolve dependency:resolve-plugins

# Copy the rest of the project files
COPY src ./src

# Clean and build the project
RUN mvn clean package -DskipTests

# Stage 2: Create a lightweight runtime image
FROM eclipse-temurin:17-jre-alpine
LABEL authors="Ayesh"

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/inventory-1.0.jar /inventory-1.0.jar

# Expose the application port
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "/inventory-1.0.jar"]
