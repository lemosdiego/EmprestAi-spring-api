# 🚀 Guia de Deploy - EmprestAi

Este documento contém o passo a passo e os comandos necessários para realizar o deploy da API **EmprestAi** no Google Cloud Run, utilizando o banco de dados Neon (PostgreSQL).

---

## 📋 Pré-requisitos

- **Google Cloud SDK (`gcloud`)** instalado e configurado.
- **Docker** instalado e rodando.
- **Projeto configurado** no Google Cloud (`api-emprestai`).
- **Repositório Docker** criado no Artifact Registry (`emprestai-repo`).
- **Segredo do Banco de Dados** configurado no Secret Manager (`NEON_DB_URL`).

---

## 🛠️ Passo a Passo para Deploy

### 1. Preparação do Ambiente (Crucial!)

Antes de construir a imagem, é necessário garantir que a aplicação esteja configurada para produção.

1.  Abra o arquivo `src/main/resources/application.properties`.
2.  **Remova** (ou comente) a linha que ativa o perfil de desenvolvimento local:
    ```properties
    # spring.profiles.active=dev  <-- REMOVER OU COMENTAR
    ```
3.  **Garanta** que as configurações de produção estejam ativas (descomentadas):
    ```properties
    spring.datasource.url=${DB_URL}
    spring.flyway.url=${DB_URL}
    spring.jpa.hibernate.ddl-auto=none
    ```

### 2. Construção da Imagem Docker

Gere uma nova versão da imagem da aplicação. O uso de `--no-cache` é recomendado para garantir que as alterações no `application.properties` sejam capturadas.

```bash
docker build --no-cache -t us-central1-docker.pkg.dev/api-emprestai/emprestai-repo/emprestai-api:latest .
```

### 3. Envio da Imagem para o Registry

Envie a imagem recém-criada para o Google Artifact Registry.

```bash
docker push us-central1-docker.pkg.dev/api-emprestai/emprestai-repo/emprestai-api:latest
```

### 4. Deploy no Cloud Run

Atualize o serviço no Cloud Run com a nova imagem. O Cloud Run injetará automaticamente a variável de ambiente `DB_URL` a partir do Secret Manager.

```bash
gcloud run deploy emprestai-api \
  --image=us-central1-docker.pkg.dev/api-emprestai/emprestai-repo/emprestai-api:latest \
  --platform=managed \
  --region=us-central1 \
  --allow-unauthenticated \
  --set-secrets="DB_URL=NEON_DB_URL:latest"
```

---

## 🔍 Verificação Pós-Deploy

Após o deploy, verifique se a aplicação subiu corretamente e se as migrações do banco de dados (Flyway) foram aplicadas.

### Ver Logs em Tempo Real

```bash
gcloud beta run services logs tail emprestai-api --region=us-central1
```

### Testar a API (Exemplo)

Substitua `URL_DO_SERVICO` pela URL fornecida no final do deploy.

```bash
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Cliente Teste Produção",
    "salarioBruto": 5000.00
  }' \
  URL_DO_SERVICO/api/simulacoes
```

---

## ⚠️ Solução de Problemas Comuns

- **Erro de Conexão com Banco (`Connection refused`):**
    - Verifique se o perfil `dev` foi removido do `application.properties`.
    - Verifique se a URL no Secret Manager (`NEON_DB_URL`) está correta e no formato JDBC (`jdbc:postgresql://...`).
    - Certifique-se de que a URL **não** contém parâmetros inválidos como `channelBinding=require`.

- **Erro de Permissão (`Permission denied on secret`):**
    - Garanta que a conta de serviço do Cloud Run tenha o papel `Secret Manager Secret Accessor`.
