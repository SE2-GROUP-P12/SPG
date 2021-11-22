package it.polito.SE2.P12.SPG.serviceTest;

import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.repository.JWTUserHandlerRepo;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import it.polito.SE2.P12.SPG.service.JWTUserHandlerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JWTUserHandlerServiceTest {
    @Autowired
    private JWTUserHandlerRepo jwtUserHandlerRepo;
    @Autowired
    private JWTUserHandlerService jwtUserHandlerService;
    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    public void initContext() {
        userRepo.deleteAll();
        User user = new User("user", "user", "user_aaaaaaaaaaaa", "", "", "user@test.com", "password");
        User tester = new User("tester", "tester", "tester_aaaaaaaaaaaa", "", "ADMIN", "tester@test.com", "password");
        userRepo.save(tester);
        userRepo.save(user);
        jwtUserHandlerRepo.deleteAll();
    }

    @Test
    public void addRelationUserTokensTest(){
        User user = userRepo.findUserByEmail("tester@test.com");
        jwtUserHandlerService.addRelationUserTokens(user, "ACCESS_TOKEN", "REFRESH_TOKEN");
        Assertions.assertTrue(jwtUserHandlerRepo.findJWTUserHandlerImplByUserId(user.getUserId()).getValid());
        Assertions.assertEquals("ACCESS_TOKEN", jwtUserHandlerRepo.findJWTUserHandlerImplByUserId(user.getUserId()).getAccessToken());
    }

    @Test
    public void invalidateUserTokensTest() throws Exception {
        User user = userRepo.findUserByEmail("tester@test.com");
        User invalidUser = userRepo.findUserByEmail("user@test.com");
        jwtUserHandlerService.addRelationUserTokens(user, "ACCESS_TOKEN", "REFRESH_TOKEN");
        jwtUserHandlerService.invalidateUserTokens(user, "ACCESS_TOKEN");
        Assertions.assertThrows(Exception.class, () -> {
            jwtUserHandlerService.invalidateUserTokens(user, "ACCESS_TOKEN_1");
        });
        Assertions.assertThrows(Exception.class, () -> {
            jwtUserHandlerService.invalidateUserTokens(invalidUser, "ACCESS_TOKEN");
        });
        Assertions.assertThrows(Exception.class, () -> {
            jwtUserHandlerService.invalidateUserTokens(invalidUser, "ACCESS_TOKEN_1");
        });
    }

    @Test
    public void checkTokenValidityTest() throws Exception {
        User user = userRepo.findUserByEmail("tester@test.com");
        User invalidUser = userRepo.findUserByEmail("user@test.com");
        jwtUserHandlerService.addRelationUserTokens(user, "ACCESS_TOKEN", "REFRESH_TOKEN");
        jwtUserHandlerService.checkTokenValidity(user, "ACCESS_TOKEN");
        Assertions.assertThrows(Exception.class, () -> {
            jwtUserHandlerService.checkTokenValidity(user, "ACCESS_TOKEN_1");
        });
        Assertions.assertThrows(Exception.class, () -> {
            jwtUserHandlerService.checkTokenValidity(invalidUser, "ACCESS_TOKEN");
        });
        Assertions.assertThrows(Exception.class, () -> {
            jwtUserHandlerService.checkTokenValidity(invalidUser, "ACCESS_TOKEN_1");
        });
        //invalidate token
        jwtUserHandlerService.invalidateUserTokens(user, "ACCESS_TOKEN");
        Assertions.assertThrows(Exception.class, () -> {
            jwtUserHandlerService.checkTokenValidity(user, "ACCESS_TOKEN");
        });
    }
}
