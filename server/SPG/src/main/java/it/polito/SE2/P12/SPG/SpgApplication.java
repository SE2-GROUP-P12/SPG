package it.polito.SE2.P12.SPG;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpgApplication {

    public static void main(String[] args) {
        System.err.println("~~~ SPG server v0.0.4 ~~~");
        SpringApplication.run(SpgApplication.class, args);
    }

}
