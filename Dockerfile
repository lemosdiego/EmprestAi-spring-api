# --- Estágio 1: Build da aplicação (Builder) ---
# Objetivo: Compilar o código Java e gerar o arquivo .jar executável.

# Usa uma imagem com o JDK 21 completo para o build.
FROM eclipse-temurin:21-jdk-jammy AS builder

# Define o diretório de trabalho dentro do container.
WORKDIR /app

# Copia todos os arquivos do projeto para o container.
COPY . .

# Executa o comando do Gradle para construir o projeto, PULANDO OS TESTES.
# Os testes falham no ambiente de build do Docker porque não há um banco de dados disponível.
RUN ./gradlew build --no-daemon -x test


# --- Estágio 2: Imagem final de execução (Runtime) ---
# Objetivo: Criar uma imagem leve e segura apenas para rodar a aplicação.

# Usa uma imagem apenas com o JRE 21, que é menor que o JDK.
FROM eclipse-temurin:21-jre-jammy

# Define o diretório de trabalho.
WORKDIR /app

# Copia o arquivo .jar gerado no estágio 'builder' para a imagem final.
COPY --from=builder /app/build/libs/*.jar app.jar

# Informa que a aplicação escutará na porta 8080.
EXPOSE 8080

# Define o comando padrão para iniciar a aplicação.
ENTRYPOINT ["java", "-jar", "app.jar"]
