# 📋 Levantamento de Requisitos
## Sistema de Simulação de Empréstimo

Sistema de simulação de empréstimo com base nas informações do cliente, utilizando inicialmente as taxas e regras do INSS.

O usuário informa **nome** e **salário bruto**, e o sistema gera **cenários de empréstimo** conforme as regras definidas pela aplicação.

---

## ✅ Requisitos Funcionais

### Funcionalidades do Sistema

- O sistema deve receber os seguintes dados do usuário:
    - Nome
    - Salário bruto

- O sistema deve calcular automaticamente o **salário líquido**, com base nas **faixas progressivas do INSS**.

- Após o cálculo do salário líquido, o sistema deve:
    - Avaliar os **cenários de crédito disponíveis**
    - Identificar em qual **faixa de crédito** o cliente se enquadra

- O sistema deve calcular o **limite de crédito** do cliente com base no salário líquido.

- O sistema deve gerar um **plano de empréstimo**, contendo:
    - Valor total liberado
    - Quantidade máxima de parcelas
    - Valor individual das parcelas
    - Taxas aplicadas

- Ao final da simulação, o sistema deve retornar ao cliente a **simulação completa**, com todos os detalhes do empréstimo.

---

## ⚙️ Requisitos Não Funcionais

### Características Técnicas

- API REST (JSON)
- Banco de dados: PostgreSQL
- Arquitetura baseada em DDD (Domain-Driven Design)
- Aplicação dos princípios SOLID
- Uso de Design Patterns
- Ambiente local containerizado com Docker

---

## 📐 Regras de Negócio

### 💳 Faixas de Crédito

| Salário Líquido (R$) | Limite de Crédito      | Máximo de Parcelas |
|----------------------|------------------------|--------------------|
| 1.599,42 a 2.000,00  | Até 3x o salário       | 12 parcelas        |
| 2.001,00 a 3.500,00  | Até 4x o salário       | 18 parcelas        |
| 3.501,00 a 4.500,00  | Até 5x o salário       | 24 parcelas        |
| Acima de 4.501,00    | Até 6x o salário       | 32 parcelas        |

---

### 📈 Taxa de Juros

- A definir

---

### 💰 Parcela Mínima

- O valor da parcela **não pode ultrapassar 20% do salário líquido** do cliente.

---

## 📌 Observações

- Todas as regras de cálculo devem estar centralizadas na camada de domínio.
- O sistema deve ser preparado para futuras alterações de taxas e regras de crédito.

## Fluxograma
![Fluxograma do sistema](https://res.cloudinary.com/dkrpmbjml/image/upload/v1771519306/Captura_de_tela_de_2026-02-19_13-40-34_hau3lv.png)
