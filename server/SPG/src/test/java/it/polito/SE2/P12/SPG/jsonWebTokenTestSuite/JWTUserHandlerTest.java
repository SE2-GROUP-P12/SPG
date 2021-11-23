package it.polito.SE2.P12.SPG.jsonWebTokenTestSuite;

import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import it.polito.SE2.P12.SPG.utils.JWTUserHandlerImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
public class JWTUserHandlerTest {
    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    public void initContext() {
        userRepo.deleteAll();
        User tester = new User("tester", "tester", "tester_aaaaaaaaaaaa", "", "ADMIN", "tester@test.com", "password");
        userRepo.save(tester);
    }

    @Test
    public void JWTUserHandlerGetterSetterTest() {
        User user = userRepo.findUserByEmail("tester@test.com");
        JWTUserHandlerImpl jwtRecord = new JWTUserHandlerImpl(user.getUserId(), "ACCESS_TOKEN", "REFRESH_TOKEN", "CREATION DATE");
        Assertions.assertEquals(user.getUserId(), jwtRecord.getUserId());
        Assertions.assertEquals("ACCESS_TOKEN", jwtRecord.getAccessToken());
        Assertions.assertTrue(jwtRecord.getValid());
    }

}
