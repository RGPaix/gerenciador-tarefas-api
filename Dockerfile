# --- Estágio 1: Build ---
# Usa uma imagem com JDK e Maven para compilar o projeto
FROM maven:3.9.5-eclipse-temurin-17 as builder
WORKDIR /app
COPY . .
# Gera o JAR, pulando os testes que rodarão na CI
RUN mvn -B package -DskipTests

# --- Estágio 2: Runtime ---
# Usa uma imagem JRE, muito mais leve, para rodar a aplicação
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Copia apenas o JAR do estágio anterior
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]