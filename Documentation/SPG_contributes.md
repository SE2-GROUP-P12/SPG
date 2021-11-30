**Table of contents**
- [Contribute](#contribute)
- [Setup dev environment](#setup-dev-environment)
  - [IDE suggestions and configuration](#ide-suggestions-and-configuration)
  - [Setup your environment](#setup-your-environment)
    - [Start the local database](#start-the-local-database)
      - [Local db in docker container with persistence](#local-db-in-docker-container-with-persistence)
        - [when to use it](#when-to-use-it)
        - [setup steps](#setup-steps)
      - [Local db in docker container without persistence](#local-db-in-docker-container-without-persistence)
        - [when to use it](#when-to-use-it-1)
        - [setup steps](#setup-steps-1)
      - [Local db not in docker container](#local-db-not-in-docker-container)
        - [when to use it](#when-to-use-it-2)
        - [setup steps](#setup-steps-2)
    - [Start the server](#start-the-server)
      - [Start the server with IDE](#start-the-server-with-ide)
      - [Start the Frontend](#start-the-frontend)
        - [Dependencies installation](#dependencies-installation)
  - [Build new docker images](#build-new-docker-images)
    - [Server docker image](#server-docker-image)
    - [Frontend docker image](#frontend-docker-image)

# Contribute

# Setup dev environment

## IDE suggestions and configuration
- IntelliJ
  - in order to import the BE maven project you have to add manually the maven project (right click the `pom.xml` file and select add maven project)
  - java 17 is required and must be linked correctly as java compiler of the spg project (we use openjdk17)

## Setup your environment

### Start the local database

There are 3 possibilities 

#### Local db in docker container with persistence

##### when to use it
This is when you want to keep the changes in your local database, in fact the container will use a persistent volume as default option in docker compose

##### setup steps
Launch the command `docker-compose up -d database`
#### Local db in docker container without persistence

##### when to use it
This is when you don't want to keep the changes in your local database, the container will not use a persistent volume and all the data will be cleaned when the container of the database is removed

##### setup steps 
Launch the command `docker-compose up -d database-test`
*The database-test is on port 13306 and has no persistent volumes*
#### Local db not in docker container

##### when to use it
if you can't use docker in your machine. In this case you can install mysql and run your own database 

##### setup steps
 -  if you are running the BE in docker container
    -  change environment variable 
      - database -> address of your mysql server
      - 3306 -> port of yuor mysql server
    ```
    environment:
      spring.datasource.url: "jdbc:mysql://database:3306/spg"
    ``` 
 - if you are running the BE locally (with your ide for example)
   - change  mysql address in `spring.datasource.url: jdbc:mysql://database:3306/spg` that you can find in the application.properties file


### Start the server

**WARNING:** In order to run the server the database must be up and running

#### Start the server with IDE

once the import of the maven project is finished, to start the application you have to start the SpgApplication (the ide should recognize it automatically, otherwise the file is in the `server/SPG/src/main/java/.../SpgApplication` )


#### Start the Frontend

**WARNING:** In order to run the frontend the database AND the server must be up and running, otherwise the frontend wont work correctly

##### Dependencies installation

Before start the frontend application you have to install the dependencies:
 -  in Frontend/spg-frontend launch this command: `npm install`
    -  *This command will check the package.json file and will download the dependencies in the `node_modules` folder*


## Build new docker images

### Server docker image

1. Go in `/server/SPG` folder, there should be the Dockerfile
   - Note: if new dependecies are added in package.json you should add in `package.docker.json` as well 
2. Execute this command `docker build --tag mattiariola/spg_server:x.x.x` where x.x.x is the version that you want to build (i.e. 1.0.0)
 - optional steps:
3. once the build is done you can select the version used by docker-compose changing the `image:` tag
4. if you wanna push the image in hubdocker repository use the command `docker push mattiariola/spg_server:x.x.x`


### Frontend docker image

*same thing as the server but you should change `server`in `frontend`*

1. Go in `/Frontend/spg-frontend` folder, there should be the Dockerfile
2. Execute this command `docker build --tag mattiariola/spg_frontend:x.x.x` where x.x.x is the version that you want to build (i.e. 1.0.0)
 - optional steps:
3. once the build is done you can select the version used by docker-compose changing the `image:` tag
4. if you wanna push the image in hubdocker repository use the command `docker push mattiariola/spg_frontend:x.x.x`

