# Idutra Service
Este projeto consiste em fornecer uma API REST para inserir, atualizar,excluir e consultar Personagens do universo de Harry Potter.
Possui integração com o a api do site [potterapi](https://www.potterapi.com) que disponibiliza uma série de informações sobre o universo de Harry Potter.

## Ferramentas de Desenvolvimento
Controle de versão utilizado no projeto é o [Git](https://git-scm.com/), especificamente o 
[GitHub](https://github.com/idutra/hp-challenge).

O projeto foi criado, basicamente com o [Java 8](https://www.oracle.com/java/technologies/java8.html), usando 
[Spring Framework](https://spring.io/projects/spring-framework) e o [Maven](https://maven.apache.org/) como controle de 
dependências.

Nesse documento é possível ver também as instruções para [montar ambiente de desenvolvimento offline](#ambiente-de-desenvolvimento-offline) 
e configurar o [Apache Maven para constuir projeto](#configurar-apache-maven).

Nessa sessão será explicado como [montar a estrutura de dados do projeto](#executar-flywaydb) utilizando [FlywayDB](https://flywaydb.org/), 
também pode-se ler algumas instruções para [gerenciar as versões do FlywayDB](#controle-flywaydb).

É importante salientar que o projeto está utilizando o [Lombok](https://projectlombok.org), portanto será necessário 
fazer a instalação do plugin do Lombok na IDE que esteja trabalhando, seja o [Eclipse](https://projectlombok.org/setup/eclipse), 
seja o [IntelliJ IDEA](https://projectlombok.org/setup/intellij) ou [MS Visual Studio Code](https://projectlombok.org/setup/vscode).

A IDE deve ser configurada para trabalhar com o encoding **UTF-8**.

## Ambiente
Para montar o ambiente é necessário o [Docker](https://www.docker.com) e o 
[docker-compose](https://docs.docker.com/compose) instalado no seu computador.

Depois da instalação do Docker e docker-compose, é necesário abrir o terminal do SO e navegar até a pasta do projeto, e 
entrar na pasta _docker_, dentro dessa pasta existe o arquivo __docker-compose.yml__, que possui as imagens do 
[MS SQL Server](https://www.microsoft.com/en-us/sql-server/sql-server-2019).

Para executar o arquivo docker-compose, basta executar o seguinte código no terminal na pasta __docker__ que existe no 
projeto
> docker-compose up

#### Banco de Dados
Certifique que no [application.yml](src/resources/application.yml) a propriedade `spring.datasource.url` 
está apontando para localhost.

## Executar FlywayDB
Para executar os scripts e montar a estrutura de dados no banco selecionado, deve-se executar o seguinte comando maven no 
terminal na raiz do projeto:
> mvn flyway:migrate -Dflyway.user=sa -Dflyway.password=teste@123 -Dflyway.url=jdbc:sqlserver://localhost;DatabaseName=id_api;sendStringParametersAsUnicode=false

## Controle FlywayDB
Todos os scripts necessários para que esse projeto funcione, devem ser adicionados na pasta _resources/db/migration_, 
seguindo a sugestão do [FlywayDB Discovery](https://flywaydb.org/documentation/migrations#discovery), seguindo o seguinte 
[padrão do FlywayDB](https://flywaydb.org/documentation/migrations#naming).

Vale salientar a importância de criar os scripts SQL usando somente SQL ANSI, visando garantir a compatibilidade com 
qualquer banco de dados ou diminuir o esforço de migrar de banco de dados.

## Swagger-UI
Para acessar a documentação online do projeto, é necessário iniciar o projeto com pelo menos um dos profiles **dev** ou **test**, 
então a documentação estará disponível em:
> http://localhost:3000/idutra/swagger-ui.html

## Gerar documentação
A documentação do projeto está sendo gerada seguindo o padrão do [OpenAPI 3.0.1](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.1.md) e 
está sendo exportada usando o [REDOC](https://github.com/Redocly/redoc/README.md), caso ainda não tenha o 
[Node.js](https://nodejs.org) instalado, instale.

### Processo Automático
Para gerar a documentação no padrão OpenAPI 3.0 é necessário executar o comando `install` com o profile `gerar-doc-openapi`  
É necessário ter o banco de dados SQL Server incializado ou o banco de dados usado na aplicação principal iniciado, pois a documentação é gerada durante a execução dos testes integrados.
A Geração da documentação é realizada através do comando maven abaixo
> mvn install -P gerar-doc-openapi

A documentação será criada na pasta [`/src/main/resources/doc/idutra-api.v${project.version}.html`](src/main/resources/doc/)

### Processo Manual
Para gerar a documentação manualmente, basta exetutar os passos a seguir

#### Instalar redoc-cli
Caso ainda não tenha redoc-cli instalado, executar o comando abaixo no terminal
> npm install -g redoc-cli

#### Exportar documentação
Levantar a aplicação, depois abrir o terminal na pasta do projeto e executar o código abaixo
>  redoc-cli bundle -o doc/idutra-api.html http://localhost:3000/idutra/v3/api-docs

