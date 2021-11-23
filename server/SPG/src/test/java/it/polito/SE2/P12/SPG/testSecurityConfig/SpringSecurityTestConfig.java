package it.polito.SE2.P12.SPG.testSecurityConfig;

import it.polito.SE2.P12.SPG.auth.UserDetailsImpl;
import it.polito.SE2.P12.SPG.entity.User;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

@TestConfiguration
public class SpringSecurityTestConfig {
    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        UserDetails tester = new UserDetailsImpl(new User("tester", "tester",
                "tester_01234567890", "1234567890", "ADMIN",
                "tester@test.com", "password"));
        UserDetails basicUser = new UserDetailsImpl(new User("user", "user",
                "user_01234567890", "1234567800", "",
                "user@test.com", "password"));
        return new InMemoryUserDetailsManager(Arrays.asList(tester, basicUser));
    }

}
