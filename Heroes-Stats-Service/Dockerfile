# Етап збірки: використання Maven для збірки додатку
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Копіюємо файли pom.xml та вихідні коди
COPY pom.xml .
COPY src ./src

# Виконуємо збірку проекту, пропускаючи тести, щоб створити JAR-файл у цільовій папці
RUN mvn clean package -DskipTests=true

# Етап виконання: використання легкого JDK 17 образу для запуску додатку
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Копіюємо зібраний jar-файл із попереднього етапу
COPY --from=build /app/target/*.jar app.jar

# Відкриваємо порт, на якому працює додаток
EXPOSE 8002

# Додаємо затримку перед запуском додатку
ENTRYPOINT ["sh", "-c", "echo 'Waiting for 10 seconds...' && sleep 10 && java -jar app.jar"]
