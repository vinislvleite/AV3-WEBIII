# AutoManager API

API REST desenvolvida em Java com Spring Boot para gerenciamento de oficinas mecânicas e lojas de autopeças.

O sistema foi construído seguindo os princípios de arquitetura REST e nível de maturidade HATEOAS, permitindo o gerenciamento completo de empresas, usuários, veículos, vendas, mercadorias, serviços e credenciais.

---

# Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Spring HATEOAS
- Maven
- MySQL

---

# Arquitetura

O projeto segue uma arquitetura em camadas:

```text
Controller -> Service -> Repository -> Database
```

Também utiliza:
- DTOs
- Relacionamentos JPA
- HATEOAS
- Boas práticas REST

---

# Como Executar

## Clonar o projeto

```bash
git clone <url-do-repositorio>
```

---

## Entrar na pasta do projeto

```bash
cd automanager
```

---

## Executar a aplicação

Linux/Mac:

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

Ou execute pela sua IDE.

---

# Base URL

```http
http://localhost:8080
```

---

# Fluxo Recomendado de Testes da API

Os IDs utilizados nos exemplos consideram um banco vazio.

A sequência abaixo segue a ordem correta de dependências entre entidades.

---

# 1. Cadastrar Empresa

```http
POST /empresa
```

```json
{
  "razaoSocial": "AutoManager Logistica LTDA",
  "nomeFantasia": "AutoManager Pro",
  "cadastro": "2026-05-09T10:00:00.000Z"
}
```

---

# 2. Cadastrar Usuários

## Cliente

```http
POST /usuario
```

```json
{
  "nome": "Vinícius Leite",
  "nomeSocial": "Vini",
  "perfis": ["CLIENTE"]
}
```

---

## Funcionário

```http
POST /usuario
```

```json
{
  "nome": "Carlos Silva",
  "nomeSocial": "Carlos",
  "perfis": ["FUNCIONARIO"]
}
```

---

# 3. Associar Usuários à Empresa

## Associar Cliente

```http
POST /empresa/1/usuario/1
```

---

## Associar Funcionário

```http
POST /empresa/1/usuario/2
```

---

# 4. Criar Credenciais

## Credencial de Senha

```http
POST /credencial-senha/usuario/1
```

```json
{
  "nomeUsuario": "vinicius",
  "senha": "123"
}
```

---

## Credencial Código de Barras

```http
POST /credencial/codigo-barra/usuario/2
```

```json
{
  "codigo": 123456789
}
```

---

# 5. Cadastrar Veículo

```http
POST /veiculo/proprietario/1
```

```json
{
  "tipo": "SUV",
  "modelo": "Toyota Corolla Cross",
  "placa": "DEV-2026"
}
```

---

# 6. Cadastrar Mercadoria

```http
POST /mercadoria
```

```json
{
  "nome": "Filtro de Óleo",
  "valor": 45.0,
  "quantidade": 50,
  "validade": "2027-01-01",
  "fabricao": "2024-01-01"
}
```

---

# 7. Cadastrar Serviço

```http
POST /servico
```

```json
{
  "nome": "Revisão Geral",
  "valor": 300.0,
  "descricao": "Check-up completo"
}
```

---

# 8. Cadastrar Venda

```http
POST /venda
```

```json
{
  "identificacao": "V-2026-001",
  "cliente": { "id": 1 },
  "funcionario": { "id": 2 },
  "veiculo": { "id": 1 },
  "mercadorias": [{ "id": 1 }],
  "servicos": [{ "id": 1 }]
}
```

---

# 9. Testar Listagens

## Empresas

```http
GET /empresa
```

---

## Usuários

```http
GET /usuario
```

---

## Veículos

```http
GET /veiculo
```

---

## Mercadorias

```http
GET /mercadoria
```

---

## Serviços

```http
GET /servico
```

---

## Vendas

```http
GET /venda
```

---

# 10. Testar Buscas por ID

## Empresa

```http
GET /empresa/1
```

---

## Usuário

```http
GET /usuario/1
```

---

## Veículo

```http
GET /veiculo/1
```

---

## Mercadoria

```http
GET /mercadoria/1
```

---

## Serviço

```http
GET /servico/1
```

---

## Venda

```http
GET /venda/1
```

---

# 11. Testar Atualizações

## Atualizar Empresa

```http
PUT /empresa/1
```

```json
{
  "nomeFantasia": "AutoManager Global"
}
```

---

## Atualizar Usuário

```http
PUT /usuario/1
```

```json
{
  "nomeSocial": "Vini Dev"
}
```

---

## Atualizar Venda

```http
PUT /venda/1
```

```json
{
  "identificacao": "V-2026-999"
}
```

---

# 12. Testar Remoções

## Remover Venda

```http
DELETE /venda/1
```

---

## Remover Serviço

```http
DELETE /servico/1
```

---

## Remover Mercadoria

```http
DELETE /mercadoria/1
```

---

## Remover Veículo

```http
DELETE /veiculo/1
```

---

## Remover Usuário

```http
DELETE /usuario/1
```

---

## Remover Empresa

```http
DELETE /empresa/1
```

---

# HATEOAS

A API utiliza HATEOAS para navegação entre recursos REST.

Exemplo de resposta:

```json
{
  "id": 1,
  "nome": "Vinícius",
  "_links": {
    "self": {
      "href": "http://localhost:8080/usuario/1"
    }
  }
}
```
