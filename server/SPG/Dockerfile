FROM openjdk:17

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline

COPY ./src/ ./src
COPY ./resources ./server/SPG/resources

CMD ["./mvnw", "spring-boot:run"]
