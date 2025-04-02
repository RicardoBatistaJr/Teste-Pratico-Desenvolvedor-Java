# Projeto - Reserva de Vagas de Estacionamento

Este projeto é uma aplicação backend desenvolvida em **Java** com **Spring Boot**, projetada para gerenciar reservas de vagas de estacionamento. Ele permite que clientes realizem reservas, acompanhem o status das vagas e consultem os custos associados de forma eficiente.

## Requisitos
Antes de iniciar, certifique-se de ter os seguintes requisitos instalados:

- **Java Development Kit (JDK 17+)**
- **Apache Maven**
- **Docker** (caso opte pela execução via contêiner)
- **Spring Boot 2.7.2**
- **Git**

## Configuração do Ambiente

### 1. Clonar o Repositório
Execute o comando abaixo para clonar o repositório e acessar o diretório do projeto:

```bash
git clone https://github.com/RicardoBatistaJr/Teste-Pratico-Desenvolvedor-Java.git
cd Teste-Pratico-Desenvolvedor-Java
```

### 2. Instalar Dependências
Dentro da raiz do projeto, execute o seguinte comando para baixar as dependências e compilar o código:

```bash
mvn clean install
```

### 3. Configurar Variáveis de Ambiente (Opcional)
Caso **não utilize Docker**, crie um arquivo `.env` na raiz do projeto e adicione as seguintes configurações:

```ini
PORT=9292
SPRING_DATASOURCE_URL=jdbc:hsqldb:file:/database/reservationdb/reservationdb;hsqldb.lock_file=false
SPRING_DATASOURCE_USERNAME=sa
SPRING_DATASOURCE_PASSWORD=sa
```

> ⚠️ **Se estiver rodando via Docker, pule esta etapa**, pois as variáveis já estarão configuradas no `docker-compose.yml`.

## Executando o Projeto

### 4.1 Executar Manualmente (Sem Docker)
Para rodar a aplicação diretamente na máquina local, utilize:

```bash
mvn spring-boot:run
```

### 4.2 Executar via Docker
Se preferir rodar a aplicação dentro de um contêiner Docker, siga os passos abaixo:

1. **Gerar o pacote do projeto:**
   ```bash
   mvn clean package
   ```

2. **Iniciar os contêineres:**
   ```bash
   docker-compose build
   ```

3. **Iniciar os contêineres:**
   ```bash
   docker-compose up
   ```

Isso irá criar e iniciar os serviços necessários para a aplicação funcionar.

## Testando a Aplicação

Para garantir que tudo está funcionando corretamente, utilize o seguinte comando:

- **Rodar os testes unitários:**
  ```bash
  mvn test
  ```

---
Agora sua aplicação está configurada e pronta para uso! 🚀

Para acessar os endpoints através de uma interface mais amigável utilize: 

http://localhost:9292/swagger-ui/index.html

---

# Endpoints

### POST - /spots
#### Cria uma nova vaga de estacionamento no sistema.
 #### Parametros
* **Sem parametros**

#### Corpo da requisição
```bash
{
  "code": "string", // Código associado a vaga de estacionamento que será criada.
  "type": "VIP", // Tipo da vaga de estacionamento - Tipos disponíveis (VIP e COMMON)
  "pricePerHour": 0 // Preço que será cobrado por hora de uso da vaga de estacionamento.
}
```
#### Responses
*  201 - Created - Vaga de estacionamento criada com sucesso
*  400 - Bad Request - Algum dos campos inseridos está inválido.
*  409 - Conflict - Algum dos campos inseridos está inválido.

#### Exemplo do corpo de resposta
```bash
{
  "id": 0,
  "code": "string",
  "type": "VIP",
  "pricePerHour": 0,
  "status": "AVAILABLE"
}
```
___

### GET - /spots/available
#### Retorna todos as vagas de estacionamento que estão com o status disponível no sistema.

#### Parametros
* **Sem parametros**.

#### Responses
*  200 - OK - Retorna a lista de vagas de estacionamento cadastradas.
*  204 - No Content - Informa que não há vagas de estacionamento cadastradas.

#### Exemplo do corpo de resposta
```bash
[
  {
    "id": 0,
    "code": "string",
    "type": "VIP",
    "pricePerHour": 0,
    "status": "AVAILABLE"
  }
]
```
---
### POST - /reservations
#### Cria uma reserva para uma vaga de estacionamente específica.
#### Parametros
* **Sem parametros**
#### Corpo da requisição
```bash
{
  "parkingSpotId": 0, // Id da vaga de estacionamento a ser reservada
  "clientId": 0, // Id do cliente solicitante da reserva
  "startTime": "2025-04-02T16:31:37.585Z" // Data para qual a vaga será reservada (Data de reserva deve ser superior a data atual)
}
```
#### Responses
*  200 - Ok - Retorna a reserva cadastrado.
*  400 - Bad Request - Algum dos campos inseridos está inválido.
*  404 - Not Found - Usuário informado não encontrado.
*  404 - Not Found - Vaga de estacionamento informada não encontrada.

#### Exemplo do corpo de resposta
```bash
{
  "id": 0,
  "startTime": "2025-04-02T16:46:03.158Z",
  "parkingSpotCode": "string",
  "client": "string"
}
```
___

### PATCH - /reservations/{id}/finish
#### Finaliza uma reserva e calcula os custos para o cliente
#### Parametros
* **id - integer ($int64)** - Id da reserva a ser finalizada
#### Corpo da requisição
* **Requisição sem corpo**

#### Responses
*  200 - Ok - Retorna os valores e tempo gasto da reserva.
*  400 - Bad Request - Algum dos campos inseridos está inválido.
*  404 - Not Found - Reserva informada não encontrada.
*  409 - Conflict - Reserva informada já está finalizada.

#### Exemplo do corpo de resposta
```bash
{
  "startTime": "2025-04-02T16:47:09.373Z",
  "endTime": "2025-04-02T16:47:09.373Z",
  "pricePerHour": 0,
  "hoursCharged": 0,
  "maintenanceFeePercentage": 0,
  "totalPrice": 0
}
```
___

### GET - /clients
#### Retorna todos os clientes cadastrados no sistema.

#### Parametros
* **Sem parametros**.

#### Responses
*  200 - OK - Retorna a lista de clientes cadastrados.
*  204 - No Content - Informa que não há clientes cadastrados.

#### Exemplo do corpo de resposta
```bash
[
  {
    "id": 0,
    "identifier": "string",
    "name": "string",
    "email": "string"
  }
]
```
---
### POST - /clients
#### Cadastra um novo cliente no sistema
#### Parametros
* **Sem parametros**
#### Corpo da requisição
```bash
{
  "identifier": "81675899021", // CPF ou CNPJ do cliente.
  "name": "string", // Nome do cliente.
  "email": "string", // Email do cliente.
  "password": "string" // Senha do cliente.
}
```
#### Responses
*  200 - Ok - Retorna o cliente cadastrado.
*  400 - Bad Request - Algum dos campos inseridos está inválido.
*  404 - Not Found - Cliente informado não encontrado.
*  409 - Conflict - Cliente cadastrado com os dados informados.

#### Exemplo do corpo de resposta
```bash
{
  "id": 0,
  "identifier": "string",
  "name": "string",
  "email": "string"
}
```
___
# Decisões arquiteturais

Compartilhando um pouco do raciocínio por trás das minhas escolhas arquiteturais para este projeto. Optei por implementar a Clean Architecture com uma abordagem orientada a casos de uso, além de utilização do docker. Aqui está o porquê:

### Clean Architecture

* ### Separação de responsabilidades
Um dos principais motivos pelos quais escolhi a Clean Architecture é a separação clara entre lógica de negócios e infraestrutura. Minha lógica de domínio não precisa saber nada sobre Spring, bancos de dados ou estruturas externas. Isso torna as principais regras de negócios muito mais fáceis de entender e manter.

* ### Testabilidade
O teste se torna muito mais fácil! Como minha lógica de negócios é isolada de dependências externas, posso escrever testes de unidade sem simular componentes pesados ​​da estrutura. Isso leva a testes mais rápidos e confiáveis ​​que se concentram em comportamentos de negócios em vez de detalhes de implementação.

* ### Modularidade e desacoplamento
A combinação de Clean Architecture com use cases melhora a modularidade, organizando o código por domínios de negócio e garantindo camadas independentes. Isso permite desenvolver de forma paralela, facilita substituir tecnologias e melhora os testes isolados e simplifica a manutenção.

### Docker
Optei por usar Docker no projeto Java com Spring para garantir um ambiente consistente e fácil de configurar, independente da máquina de cada desenvolvedor. Com Docker, eliminamos o famoso "funciona na minha máquina" e ganhamos praticidade ao subir a aplicação e seus serviços, variáveis de ambiente e algo como banco de dados.


# Diagrama do banco de dados

<p align="center">
<img src="database/Diagram.png" alt="Database Image" width="600"/>
</p>