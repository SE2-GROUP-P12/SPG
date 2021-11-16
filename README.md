# SPG
SPG SE2_Project

- [SPG](#spg)
- [BE instructions](#be-instructions)
  - [Requirement](#requirement)
  - [Run the services in your local machine](#run-the-services-in-your-local-machine)
    - [Connect to your local DB](#connect-to-your-local-db)
    - [Reset the local DB](#reset-the-local-db)
  - [Run the server](#run-the-server)
- [FE instructions](#fe-instructions)
  - [Install FE dependencies](#install-fe-dependencies)
  - [Run FE application](#run-fe-application)

full docs in documentation folder

# BE instructions
## Requirement

 + docker running and installed on the host machine
 + java 11 jdk (we use amazon corretto)

## Run the services in your local machine

Let's run the docker containers for the services needed by our server application

 1. go in the same folder of our docker-compose.yml file
 2. run the command  `docker-compose up -d` (sudo may be needed)

Note: the container with "test" in the name are used in the tests
### Connect to your local DB

The server should automatically connect to the db using the `application.properties`, if you want have acces to the db using the credential in the docker-compose

### Reset the local DB
 
 if you want reset the local db you can simply run the commands 
 1. `docker-compose down` 
 2. `docker prune volumes` (this will remove all the not used volumes, if you want you can only remove the `mysql-persistent-volume`)


## Run the server

You can simply use an ide like IntelliJ and run the `SpgApplication` in our server/src folder

 + WARNING: you may need to add the maven project selecting the `pom.xml` file in server/spg

# FE instructions

go in fronted/spg-frontend  folder
## Install FE dependencies

 + use the command `npm install`to install in your local machine the node_modules needed to run the react application

## Run FE application

 + once the installation is completed use the command `npm start` to start the react application

