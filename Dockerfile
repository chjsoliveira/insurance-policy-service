# Etapa 1 - Build da aplicação com JDK 11
FROM eclipse-temurin:11-jdk-alpine AS builder

WORKDIR /app

# Copia os arquivos do projeto
COPY . .

# Permissão para o Gradle wrapper
RUN chmod +x ./gradlew

# Compila a aplicação sem testes
RUN ./gradlew clean build -x test

# Etapa 2 - Runtime leve com JDK 11
FROM eclipse-temurin:11-jdk-alpine

WORKDIR /app

# Copia o JAR compilado
COPY --from=builder /app/build/libs/*.jar app.jar

# Expõe a porta da aplicação
EXPOSE 8080

# Executa a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
