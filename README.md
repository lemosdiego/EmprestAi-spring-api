# EmprestAi - API de Simulação de Empréstimos

> ℹ️ **Nota:** Este projeto utiliza nomes de classes, métodos e variáveis em português para facilitar o aprendizado e a compreensão do domínio de negócio, seguindo padrões de mercado.

O **EmprestAi** é uma API REST desenvolvida em Java com Spring Boot, responsável por realizar simulações de empréstimos pessoais. O sistema aplica regras de negócio baseadas no salário líquido do cliente (após desconto do INSS) para determinar limites de crédito e condições de parcelamento.

O projeto foi construído com foco em **boas práticas de desenvolvimento**, **arquitetura limpa (DDD)** e **testabilidade**.

---

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3** (Web, Data JPA, Validation)
- **PostgreSQL** (Banco de Dados)
- **Flyway** (Migração de Banco de Dados)
- **Docker & Docker Compose** (Containerização)
- **JUnit 5** (Testes)

---

## 🏗️ Arquitetura

O projeto segue os princípios do **Domain-Driven Design (DDD)**, organizado nas seguintes camadas:

- **Domain:** Contém as entidades (`Cliente`, `Simulacao`), enums (`FaixaInss`, `FaixaCredito`) e as regras de negócio puras (`CalculoSalarioLiquidoRegra`, `SimulaEmprestimoRegra`). Esta camada não depende de frameworks externos.
- **Application:** Contém os casos de uso (`SimularEmprestimoUseCase`) e os controladores REST (`SimularEmprestimoController`), orquestrando o fluxo de dados.
- **Infrastructure:** Implementação de persistência (`Repositories`, `JPA Entities`) e configurações de banco de dados.

---

## ⚙️ Como Executar o Projeto

### Pré-requisitos

- Docker e Docker Compose instalados
- JDK 21 (opcional, caso queira rodar fora do Docker)

### Passo a Passo

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/seu-usuario/emprestai.git
   cd emprestai
   ```

2. **Suba o ambiente com Docker Compose:**
   Este comando iniciará o banco de dados PostgreSQL e a aplicação.
   ```bash
   docker-compose up -d
   ```

3. **Aguarde a inicialização:**
   A aplicação estará disponível em `http://localhost:8080`.

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
      // ... outros planos disponíveis
    ]
  }
}
```

---

## 📐 Regras de Negócio

### 1. Cálculo do Salário Líquido (INSS)
O sistema calcula o desconto do INSS de forma progressiva, utilizando as faixas oficiais (exemplo de 2024):
- Até R$ 1.412,00: **7,5%**
- De R$ 1.412,01 a R$ 2.666,68: **9%**
- De R$ 2.666,69 a R$ 4.000,03: **12%**
- De R$ 4.000,04 a R$ 7.786,02: **14%**

### 2. Faixas de Crédito
Com base no salário líquido, o cliente é enquadrado em uma categoria que define o limite de crédito e o prazo máximo:

| Salário Líquido (R$) | Limite de Crédito | Máx. Parcelas |
|----------------------|-------------------|---------------|
| 1.599,42 - 2.000,00  | Até 3x o salário  | 12x           |
| 2.001,00 - 3.500,00  | Até 4x o salário  | 18x           |
| 3.501,00 - 4.500,00  | Até 5x o salário  | 24x           |
| Acima de 4.501,00    | Até 6x o salário  | 32x           |

### 3. Regra da Parcela
O valor da parcela mensal **não pode ultrapassar 20%** do salário líquido do cliente, garantindo a saúde financeira do tomador de crédito.

---

## 🛠️ Desenvolvimento e Testes

Para rodar os testes unitários e de integração:

```bash
./gradlew test
```

Para gerar o build do projeto:

```bash
./gradlew build
```
