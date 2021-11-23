package it.polito.SE2.P12.SPG.authenticationInterfaceTest;

import it.polito.SE2.P12.SPG.auth.UserDetailsImpl;
import it.polito.SE2.P12.SPG.auth.UserDetailsServiceImpl;
import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.sql.SQLException;

@SpringBootTest
public class UserDetailsTest {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void initContext() throws SQLException {
        userRepo.deleteAll();
        User tester = new User("tester", "tester", "tester_aaaaaaaaaaaa", "", "ADMIN", "tester@test.com", "password");
        userRepo.save(tester);
    }

    @AfterEach
    public void restDB(){
        userRepo.deleteAll();
    }

    @Test
    public void userDetailsServiceImplTest() {
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername("tester@test.com");
        Assertions.assertNotNull(userDetails);
    }

    @Test
    public void userDetailsServiceImplErrorHandlerTest() {
        Exception e = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername("");
        });
        Assertions.assertEquals("User not found: ", e.getMessage());
        e = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(null);
        });
        Assertions.assertEquals("User not found: email is null", e.getMessage());
    }

    @Test
    public void UserDetailsImpGetterSetterTest() {
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername("tester@test.com");
        Assertions.assertEquals("password", userDetails.getPassword());
        Assertions.assertEquals("tester@test.com", userDetails.getUsername());
        Assertions.assertEquals("tester", userDetails.getName());
        Assertions.assertEquals("[customer:read, ROLE_ADMIN]", userDetails.getAuthorities().toString());
        Assertions.assertTrue(userDetails.isAccountNonLocked());
        Assertions.assertTrue(userDetails.isEnabled());
        Assertions.assertTrue(userDetails.isAccountNonExpired());
        Assertions.assertTrue(userDetails.isCredentialsNonExpired());
    }

}
