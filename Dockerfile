# 1단계: 빌드
FROM gradle:8.4-jdk21 AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test --no-daemon

# 2단계: 실행
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]
