# EmprestAi - API de Simulação de Empréstimos

> ℹ️ **Nota:** Este projeto utiliza nomes de classes, métodos e variáveis em português para facilitar o aprendizado e a compreensão do domínio de negócio, seguindo padrões de mercado.

O **EmprestAi** é uma API REST desenvolvida em Java com Spring Boot, responsável por realizar simulações de empréstimos pessoais. O sistema aplica regras de negócio baseadas no salário líquido do cliente (após desconto do INSS) para determinar limites de crédito e condições de parcelamento.

O projeto foi construído com foco em **boas práticas de desenvolvimento**, **arquitetura limpa (DDD)** e **infraestrutura Cloud-Native**.

---

## 🚀 Tecnologias e Ferramentas

### Backend
- **Java 21** (LTS)
- **Spring Boot 3** (Web, Data JPA, Validation)
- **Flyway** (Migração de Banco de Dados)
- 
#### Próxima etapa de aprendizado 
- **JUnit 5** (Testes Automatizados)
- 
### Infraestrutura & Cloud (Serverless)
- **Google Cloud Run:** Plataforma de container serverless para hospedar a API.
- **Neon (PostgreSQL):** Banco de dados PostgreSQL serverless, separando computação de armazenamento.
- **Google Artifact Registry:** Armazenamento seguro das imagens Docker.
- **Google Secret Manager:** Gerenciamento seguro de credenciais (URL do banco de dados).
- **Docker:** Containerização da aplicação com *Multi-Stage Build* para imagens otimizadas.

### Desenvolvimento Local
- **Docker Compose:** Orquestração do ambiente de desenvolvimento local (PostgreSQL).

---

## 🏗️ Arquitetura

O projeto segue os princípios do **Domain-Driven Design (DDD)**, organizado nas seguintes camadas:

- **Domain:** Contém as entidades (`Cliente`, `Simulacao`), enums (`FaixaInss`, `FaixaCredito`) e as regras de negócio puras.
- **Application:** Contém os casos de uso (`SimularEmprestimoUseCase`) e os controladores REST.
- **Infrastructure:** Implementação de persistência e configurações de banco de dados.

---

## ⚙️ Como Executar o Projeto

### 1. Ambiente Local (Desenvolvimento)

Para rodar a aplicação localmente, utilizamos o Docker Compose para subir o banco de dados e configuramos a aplicação para usar o perfil `dev`.

**Pré-requisitos:** Docker e JDK 21 instalados.

1. **Suba o banco de dados local:**
   ```bash
   docker-compose up -d
   ```

2. **Execute a aplicação:**
   Você pode rodar via IDE (IntelliJ/Eclipse) ativando o perfil `dev` ou via terminal:
   ```bash
   ./gradlew bootRun --args='--spring.profiles.active=dev'
   ```
   
   *A API estará disponível em `http://localhost:8080`.*

### 2. Ambiente de Produção (Cloud Run)

O deploy é realizado via Google Cloud CLI (`gcloud`). A aplicação é empacotada em um container Docker e implantada no Cloud Run, conectando-se ao banco Neon via variáveis de ambiente seguras.

**Fluxo de Deploy:**
1. Build da imagem Docker.
2. Push para o Artifact Registry.
3. Deploy no Cloud Run injetando a secret `DB_URL`.

```bash
# Exemplo de comando de deploy
gcloud run deploy emprestai-api \
  --image=us-central1-docker.pkg.dev/SEU_PROJETO/emprestai-repo/emprestai-api:latest \
  --set-secrets="DB_URL=NEON_DB_URL:latest"
```

---

## 📖 Documentação da API

### Simular Empréstimo

Realiza uma simulação de crédito com base nos dados do cliente.

**Endpoint:** `POST /api/simulacoes`

#### Exemplo de Requisição (Request)

```json
{
  "nome": "João da Silva",
  "salarioBruto": 5000.00
}
```

#### Exemplo de Resposta (Response)

```json
{
  "cliente": {
    "nome": "João da Silva",
    "salarioBruto": 5000.00,
    "salarioLiquido": 4168.12
  },
  "simulacao": {
    "limiteCredito": 25008.72,
    "planos": [
      {
        "parcelas": 12,
        "valorParcela": 2084.06
      },
      {
        "parcelas": 24,
        "valorParcela": 1042.03
      }
    ]
  }
}
```

---

## 📐 Regras de Negócio

### 1. Cálculo do Salário Líquido (INSS)
O sistema calcula o desconto do INSS de forma progressiva.

### 2. Faixas de Crédito
Com base no salário líquido, o cliente é enquadrado em uma categoria que define o limite de crédito e o prazo máximo.

| Salário Líquido (R$) | Limite de Crédito | Máx. Parcelas |
|----------------------|-------------------|---------------|
| 1.599,42 - 2.000,00  | Até 3x o salário  | 12x           |
| 2.001,00 - 3.500,00  | Até 4x o salário  | 18x           |
| 3.501,00 - 4.500,00  | Até 5x o salário  | 24x           |
| Acima de 4.501,00    | Até 6x o salário  | 32x           |

### 3. Validação Mínima
O sistema não processa simulações para salários brutos inferiores a **R$ 1.621,00**.

---

## 🛠️ Comandos Úteis

- **Rodar Testes:** `./gradlew test`
- **Gerar Build:** `./gradlew build`
- **Limpar Projeto:** `./gradlew clean`
