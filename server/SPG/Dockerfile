FROM openjdk:17

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline

COPY ./src/ ./src
COPY ./resources ./resources

CMD ["./mvnw", "spring-boot:run"]
