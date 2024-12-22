# MaxDoc Simplificado - Backend

MaxDoc Simplificado é um projeto para gerenciamento simplificado de documentos, composto por dois repositórios: **Backend** e **Frontend**.

---

## Repositório Backend

O backend é construído com **Spring Boot** e utiliza **PostgreSQL** como banco de dados.

### Tecnologias Utilizadas
- Java 23
- Spring Boot 3.4.0
- Spring Data JPA
- Spring Security
- PostgreSQL
- Lombok
- JWT Authentication

### Configuração

#### Arquivo `pom.xml`
Inclui as dependências principais como:
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-security`
- `spring-boot-starter-web`
- `spring-boot-devtools`
- `postgresql`
- `lombok`
- `java-jwt`


#### Arquivo `application.properties`
    properties
    spring.application.name=maxdoc
    spring.datasource.url=jdbc:postgresql://localhost:5432/maxdoc_db
    spring.datasource.username=postgres
    spring.datasource.password=postgres
    spring.jpa.hibernate.ddl-auto=update
    spring.datasource.driver-class-name=org.postgresql.Driver
    logging.level.org.springframework.security=DEBUG
    api.security.token.secret=my-secret-maxdox

### Passos para executar o backend

Clone o repositório backend:

    git clone <URL_DO_REPOSITORIO_BACKEND>

### Configure o banco de dados PostgreSQL conforme o arquivo application.properties.
Execute o projeto:

    mvn spring-boot:run

