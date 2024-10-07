# Stage 1: Build the application
FROM maven:3.9.8-eclipse-temurin-17 AS builder

# Tạo thư mục làm việc cho quá trình build
WORKDIR /build

# Sao chép toàn bộ mã nguồn vào container
COPY . .

# Chạy lệnh Maven để build ứng dụng và tạo file JAR
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-jammy AS final

# Tạo thư mục làm việc cho quá trình chạy
WORKDIR /app

# Sao chép tệp JAR đã được tạo ở stage 1
COPY --from=builder /build/target/globie-0.0.1-SNAPSHOT.jar /app/app.jar

# Mở cổng mà ứng dụng sẽ lắng nghe
EXPOSE 8080

# Thiết lập lệnh khởi động ứng dụng
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
