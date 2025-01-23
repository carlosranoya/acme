# Desafio ACME - Projeto de simulação de solicitações de cotação de apólices de seguro


O objetivo da solução é de disponibilizar uma API Rest simples para vibilizar este cenário, mas conta com o desafio de consultas em múltiplos serviços, agregação de dados, várias regras de validação de reuqisição, persistência de dados e comunicação assíncrona via message-broker.


## Componentes do sistema

1. **/quote** - A aplicação principal foi construída em Java 17 e SpringBoot. A principal finalidade é servir requisições Http/REST por meio de 2 endpoints, um para solicitação e outro para consulta. Os dados são persistidos em um banco MongoDB.
2. **/catalog-mock-server** - Aplicação necessária para as consultas sobre produtos e ofertas. Foi construído em Python/Flask, e devolve dados mockados em 2 arquivos json que acompanham a aplicação.
3. **/policy-mock-server** - Aplicação que vai receber notificação sobre a solicitação de cotação de seguro após os dados serem validados, vai gerar um id único para a apólice e notificará a aplicação principal. A comunicação entre elas é viabilizada por meio do message-broker, no caso o RabbitMQ.
4. **/auth-mock-server** - Apesar da proposta não falar nada a respeito de autenticação, tomei a decisão de incluir um serviço para esta finalidade, também simples e mockado, que vai validar alguns tokens de acesso guardados no arquivo único da aplicação, que também foi construída em Python/Flask. Uma situação possível para um cenário como este seria o contexto em que o cliente do serviço gera uma chave de autenticação JWT em um serviço específico e depois as requisições nos outros serviços precisam validar a autenticidade desta chave.
5. **Observalidade** - O sistema foi preparado para rodar localmente em docker, e junto com as aplicações customizadas estão rodando algumas ferramentas de observalidade: Prometheus (banco de dados TSBD), Grafana (feramenta de visualização de dados, compatível com o Prometheus) e o cAdvisor, que é uma ferramenta do Google que monitora o desempenho de contêineres.
6. **Mongo Express** - como brinde, acomanha também (brinde) este cliente de MongoDB que roda no browser


## 📋 Pré-requisitos

Para rodar o sistema, basta ter o [Docker](https://www.docker.com/products/docker-desktop/) instalado.

Para desenvolver, a susgetão, mas não obrigatório é que tenha instalado:
* [Maven](https://maven.apache.org/)
* [Java 17 ou superior](https://www.oracle.com/java/technologies/downloads/)

## 🚀 Run

Com a aplicação na máquina local, basta rodar na raiz do projeto:

`docker compose up --build`

Para desenvolvimento, com as ferramentas de observabilidade desligadas, pode rodar os serviços mínimos:

`docker compose up -f docker-compose-min.yml --build`

E rodar localmente a aplicação **/quote** com sua IDE preferencial, ou pela linha de comando:

`java -jar ./quote/target/quote-0.0.1-SNAPSHOT.jar`

Para compilar **/quote** é só ir até o diretório da aplicação:

`cd quote`

E compliar com o Maven:

`mvn clean install`

O projeto inclui configurações para rodar dentro do Visual Studio Code. As opções estão no arquivo em ".vscode/launch.json"


## Racional

Primeiramente sobre as escolhas feitas.

A escolha de um banco NoSql se deve ao fato de, justamente, não haver necessidade de relacionamentos entre entidades do banco. Há apenas 3 requerimentos para o BD: gravar um documento, consultar e fazer um uapdatede um campo (id da apólice).
Os servidores rest mockados foram feitos em Python/Flask pela enorme facilidade em se implementar. Poucas linhas de código.

A escolha do ambiente Docker para rodar os containers também de deve à maior facilidade de implantação, na minha opinião, pela necessidade de subir todos os 11 serviços (bancos inclusos).

A escolha do RabbitMQ como message broker também é justificável pela natureza das mensagens: pequenas e com suposta baixa demanda em uma situação real.

Sobre a arquitetura da aplicação principal, houve uma preocupação em adotar uma extrutura de "Clean Architecture", mantendo clara a separação entre camada de domínio, de aplicação e de recursos externos e suas conexões. Então o projeto está também aderente ao modelo hexagonal, evidenciado pelas definições de portas de saída e entrada como interfaces a serem implemetadas dentro camada de aplicação e entre a camada de aplicação e a infraestrutura.

Mais um pequeno detalhe, houve a inserção de atrasos (Thread.sleep()) na comunicação com broker, para "simular" um comportamento de processamento mais lento dos dados trocados.

## Pendências

Era o desejo inicial avaliar a cobertura de testes da aplicação principal pelo SonarQube, mas por limitações de tempo, esta ficou sendo uma pendência importante.

As ferrmentas de obsrvabilidade estão também no momento só como recursos exemplificativos, pois a instrumentalização da aplicação também ficou comprometida pelo prazo de conclusão.

Ficou pendente também a documentação em Swagger, também um desejo inicial.

Para o trace das requisições, ficou no papel a implementação de um mecanismo de levar um correlation_id único, que é passado de uma aplicação para outra, possibilitando o rastreamento das requisições relacionadas, em combinação com os logs das requisições.

## ✒️ Autor

[Carlos Ranoya](www.linkedin.com/in/carlos-ranoya-8ab0ba22)


## 📄 Extras

Para as aplicações http/rest, as rotas http estão documentadas [aqui](https://documenter.getpostman.com/view/11964445/2sAYQdkABK).

Acompanha o projeto também uma collection do Postman com estas APIs. (/extras)

Para acesso às ferramentas que compõem o projeto, seguem as instruções, somente acessíveis com os containers todos rodando.

1. cAdivisor:

http://127.0.0.1:8080/

repositório: https://github.com/google/cadvisor

2. Grafana:

http://127.0.0.1:3000/login

user: admin
pass: admin

sugestão para melhoria: instalar o Loki para Grafana, poderosa ferramenta para análise de logs
https://grafana.com/docs/loki/latest/setup/install/docker/

3. Mongo Express:

http://127.0.0.1:8081/

user: user
pass: secret

4. Admin do RabbitMQ

http://127.0.0.1:8085

user: guest
pass: guest
