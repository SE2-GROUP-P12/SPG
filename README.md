**Table of contents**
- [SPG Team P12](#spg-team-p12)
- [Docs guide](#docs-guide)
- [Running the application in container](#running-the-application-in-container)
  - [Configure the server](#configure-the-server)
    - [Reset the local DB](#reset-the-local-db)
  - [Set Up Email Server](#set-up-email-server)
  - [Run the server](#run-the-server)
- [FE instructions](#fe-instructions)
  - [Install FE dependencies](#install-fe-dependencies)
  - [Run FE application](#run-fe-application)
# SPG Team P12
Members:
 - Gracis Riccardo (s287961)
 - Giuseppe Gagliardi (s286286)
 - Marco Riggio (s292515)
 - Martina Saugo (s285326)
 - Mattia Riola (s280169)
 - Stefano Griva (s287729)

# Docs guide

The full documents are in the `Documents` folder

[developer environment](/Documentation/SPG_contributes.md)
[time machine developer](/Documentation/TimeMachineManual.md)
[deploy and configuration](/Documentation/SPG_deploy.md)


# Running the application in container
requirement: 
  - docker installed
steps:

- clone this repository
- be sure that the following ports are free: 8080, 3000, 3306.
  - if you want change the listening port in our application you can copy the `docker-compose.template.yourtemplate.yml` in a new file called `docker-compose.override.yml` and change the settings that you want
- Run the application: `docker-compose up -d` (you may need admin privileges)
  - this command will download our docker images and will run all the services needed by the application (DB, FE, BE)
  - to be sure that the service are running you can use the command `docker ps -a` and check the status column
  - in order to see the logs of the services use the `docker logs -f service_name` command (where service_name can be `spg_server` `spg_database`or `spg_frontend`)



## Configure the server

### Reset the local DB
 
 if you want reset the local db you can simply run the commands 
 1. shut down the services and remove the container, you can do this with this commang: `docker-compose down` 
 2. execute this command to remove the volume used by mysql: `docker volume rm spg_mysql-persistent-volume`

### Set Up Email Server

  Currently the email server is linked to a MailTrap account (s287961@studenti.polito.it), the number of mail available is 500 per month.
  By the way the mail server(which is using SMTP) could be set up using other credentials and/or mail server (if yoo want to use a real mail server a security channel on TLS should be used).
  The `application.properties` file in the `main` package can be edited in order to seu up correctly the mail server.
  Pay attention to not edit the `application.properties` file in the `test` package since this is working on greenmail SMTP mock, if wrongly edited some test will crash.


## Run the server

You can simply use an ide like IntelliJ and run the `SpgApplication` in our server/src folder

 + WARNING: you may need to add the maven project selecting the `pom.xml` file in server/spg

# FE instructions

go in fronted/spg-frontend  folder
## Install FE dependencies

 + use the command `npm install`to install in your local machine the node_modules needed to run the react application

## Run FE application

 + once the installation is completed use the command `npm start` to start the react application

## Credentials:
  - customer: 
    - username: mario.rossi@gmail.com
    - password: password
 - shop employee: 
    - username: francesco.conte@gmail.com
    - password: password
 - farmer: 
    - username: thomas.jefferson@gmail.com
    - password: password

