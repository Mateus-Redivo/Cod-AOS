# API REST para Gerenciamento de Produtos

Este projeto é uma API RESTful desenvolvida com Spring Boot para gerenciamento de produtos, oferecendo operações CRUD (Create, Read, Update, Delete) completas.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.4.5
- Spring Data JPA: Para persistência de dados
- Spring MVC: Para construção da API REST
- MySQL: Como banco de dados relacional (via Docker)
- Docker: Para containerização do banco de dados
- Maven: Para gerenciamento de dependências e build

## Estrutura do Projeto

O projeto segue uma arquitetura em camadas:

- Model: Entidades JPA que representam tabelas do banco de dados
- DTO: Objetos de transferência de dados para comunicação com o cliente
- Repository: Interfaces para acesso aos dados
- Service: Camada de negócios
- Controller: Endpoints da API REST

## Configuração do Ambiente

### Docker Compose

O arquivo `docker-compose.yml` na raiz do projeto configura o MySQL:

```yaml
services:
  mysql:
    image: mysql:9.3.0
    container_name: api_rest_mysql
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: r00t_P@ssw0rd!
      MYSQL_DATABASE: api_rest_db
      MYSQL_USER: api_user
      MYSQL_PASSWORD: Secure_P@ss123!
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    networks:
      - api_network

volumes:
  mysql_data:

networks:
  api_network:
    driver: bridge
```

### Iniciar o Banco de Dados

```bash
docker-compose up -d
```

### Configuração da Conexão

As configurações de conexão com o banco de dados estão no arquivo `application.properties`:

```properties
# DataSource Configuration (MySQL)
spring.datasource.url=jdbc:mysql://localhost:3306/api_rest_db
spring.datasource.username=api_user
spring.datasource.password=Secure_P@ss123!
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

## Endpoints da API

A API expõe os seguintes endpoints para gerenciamento de produtos:

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/produtos` | Lista todos os produtos |
| GET | `/api/produtos/{id}` | Obtém um produto específico pelo ID |
| POST | `/api/produtos` | Cria um novo produto |
| PUT | `/api/produtos/{id}` | Atualiza um produto existente |
| DELETE | `/api/produtos/{id}` | Remove um produto |

## Testando a API

### Hoppscotch

Para testar a API, utilizamos o [Hoppscotch](https://hoppscotch.io/), uma ferramenta gratuita e de código aberto para testes de API. 

Para começar:
1. Acesse [https://hoppscotch.io/](https://hoppscotch.io/)
2. No painel esquerdo, selecione o método HTTP desejado
3. Insira a URL base: `http://localhost:8080`

### Exemplos de Requisições no Hoppscotch

#### Listar Produtos (GET)
- URL: `http://localhost:8080/api/produtos`
- Método: GET
- Headers: 
  ```
  Content-Type: application/json
  ```

#### Criar Produto (POST)
- URL: `http://localhost:8080/api/produtos`
- Método: POST
- Headers:
  ```
  Content-Type: application/json
  ```
- Body:
  ```json
  {
    "nome": "Produto Teste",
    "descricao": "Descrição do produto teste",
    "preco": 99.90,
    "quantidade": 10
  }
  ```

#### Atualizar Produto (PUT)
- URL: `http://localhost:8080/api/produtos/{id}`
- Método: PUT
- Headers:
  ```
  Content-Type: application/json
  ```
- Body:
  ```json
  {
    "nome": "Produto Atualizado",
    "descricao": "Descrição do produto atualizada",
    "preco": 199.90,
    "quantidade": 20
  }
  ```

#### Deletar Produto (DELETE)
- URL: `http://localhost:8080/api/produtos/{id}`
- Método: DELETE

#### Buscar Produto por ID (GET)
- URL: `http://localhost:8080/api/produtos/{id}`
- Método: GET
- Headers:
  ```
  Content-Type: application/json
  ```

### Dicas para Testes no Hoppscotch
- Use a funcionalidade de "Collections" para salvar suas requisições
- Utilize variáveis de ambiente para a URL base
- Verifique os códigos de status HTTP retornados
- Observe o tempo de resposta das requisições
- Use o histórico de requisições para debug

## Modelo de Dados

### Produto
- id: Long - Identificador único
- nome: String - Nome do produto
- descricao: String - Descrição detalhada do produto
- preco: Double - Preço do produto
- quantidade: Integer - Quantidade em estoque

## Características da Implementação

- Padrão DTO: Utilização de DTOs para separação entre modelo de domínio e representação externa
- Injeção de Dependência: Uso de constructor injection para melhor testabilidade
- Tratamento de Erros: Respostas HTTP apropriadas para diferentes situações
- Mapeamento Objeto-Relacional: Através de anotações JPA

## Como Executar

1. Clone o repositório
2. Certifique-se de ter o Docker instalado
3. Inicie o container MySQL:
```bash
docker-compose up -d
```
4. Execute o aplicativo usando Maven:
```bash
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`

## Requisitos

- JDK 21 ou superior
- Docker e Docker Compose
- Maven 3.6 ou superior

---

Desenvolvido como um exemplo de API REST utilizando Spring Boot e MySQL com Docker.
