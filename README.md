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

```bash
./mvnw spring-boot:run ou pelo botão da sua IDE
```

Ou no Windows:

```bash
mvnw.cmd spring-boot:run ou pelo botão da sua IDE
```

---

# Base URL

```http
http://localhost:8080
```

---

# Empresa

**Base URL:** `/empresa`

## Listar empresas

```http
GET /empresa
```

---

## Buscar empresa por ID

```http
GET /empresa/{id}
```

---

## Cadastrar empresa

```http
POST /empresa
```

### Body

```json
{
  "razaoSocial": "AutoManager Logistica LTDA",
  "nomeFantasia": "AutoManager Pro",
  "cadastro": "2026-05-09T10:00:00.000Z"
}
```

---

## Atualizar empresa

```http
PUT /empresa/{id}
```

### Body

```json
{
  "nomeFantasia": "AutoManager Global"
}
```

---

## Remover empresa

```http
DELETE /empresa/{id}
```

---

## Associar usuário à empresa

```http
POST /empresa/{empresaId}/usuario/{usuarioId}
```

### Exemplo

```http
POST /empresa/1/usuario/1
```

---

## Desassociar usuário da empresa

```http
DELETE /empresa/{empresaId}/usuario/{usuarioId}
```

### Exemplo

```http
DELETE /empresa/1/usuario/1
```

---

# Usuário

**Base URL:** `/usuario`

## Listar usuários

```http
GET /usuario
```

---

## Buscar usuário por ID

```http
GET /usuario/{id}
```

---

## Cadastrar usuário

```http
POST /usuario
```

### Body

```json
{
  "nome": "Vinícius Leite",
  "nomeSocial": "Vini",
  "perfis": ["CLIENTE", "FUNCIONARIO"]
}
```

---

## Atualizar usuário

```http
PUT /usuario/{id}
```

### Body

```json
{
  "nomeSocial": "Vini Dev"
}
```

---

## Remover usuário

```http
DELETE /usuario/{id}
```

---

# Veículo

**Base URL:** `/veiculo`

## Listar veículos

```http
GET /veiculo
```

---

## Buscar veículo por ID

```http
GET /veiculo/{id}
```

---

## Cadastrar veículo

```http
POST /veiculo/proprietario/{proprietarioId}
```

### Body

```json
{
  "tipo": "CARRO",
  "modelo": "Samsung Odyssey G3 Edition",
  "placa": "DEV-2026"
}
```

---

## Atualizar veículo

```http
PUT /veiculo/{id}
```

---

## Remover veículo

```http
DELETE /veiculo/{id}
```

---

# Venda

**Base URL:** `/venda`

## Listar vendas

```http
GET /venda
```

---

## Buscar venda por ID

```http
GET /venda/{id}
```

---

## Cadastrar venda

```http
POST /venda
```

### Body

```json
{
  "identificacao": "V-2026-001",
  "clienteId": 1,
  "funcionarioId": 2,
  "veiculoId": 1,
  "mercadoriaIds": [1],
  "servicoIds": [1]
}
```

---

## Atualizar venda

```http
PUT /venda/{id}
```

---

## Remover venda

```http
DELETE /venda/{id}
```

---

# Mercadoria

**Base URL:** `/mercadoria`

## Listar mercadorias

```http
GET /mercadoria
```

---

## Buscar mercadoria por ID

```http
GET /mercadoria/{id}
```

---

## Cadastrar mercadoria

```http
POST /mercadoria
```

### Body

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

## Atualizar mercadoria

```http
PUT /mercadoria/{id}
```

---

## Remover mercadoria

```http
DELETE /mercadoria/{id}
```

---

# Serviço

**Base URL:** `/servico`

## Listar serviços

```http
GET /servico
```

---

## Buscar serviço por ID

```http
GET /servico/{id}
```

---

## Cadastrar serviço

```http
POST /servico
```

### Body

```json
{
  "nome": "Revisão Geral",
  "valor": 300.0,
  "descricao": "Check-up completo"
}
```

---

## Atualizar serviço

```http
PUT /servico/{id}
```

---

## Remover serviço

```http
DELETE /servico/{id}
```

---

# Credenciais

## Credencial de Senha

**Base URL:** `/credencial-senha`

### Listar credenciais

```http
GET /credencial-senha
```

---

### Buscar credencial por ID

```http
GET /credencial-senha/{id}
```

---

### Criar credencial

```http
POST /credencial-senha/usuario/{usuarioId}
```

---

### Atualizar credencial

```http
PUT /credencial-senha/{id}
```

---

### Remover credencial

```http
DELETE /credencial-senha/{id}
```

---

# Credencial Código de Barras

**Base URL:** `/credencial/codigo-barra`

### Listar credenciais

```http
GET /credencial/codigo-barra
```

---

### Buscar credencial por ID

```http
GET /credencial/codigo-barra/{id}
```

---

### Criar credencial

```http
POST /credencial/codigo-barra/usuario/{usuarioId}
```

---

### Atualizar credencial

```http
PUT /credencial/codigo-barra/{id}
```

---

### Remover credencial

```http
DELETE /credencial/codigo-barra/{id}
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
