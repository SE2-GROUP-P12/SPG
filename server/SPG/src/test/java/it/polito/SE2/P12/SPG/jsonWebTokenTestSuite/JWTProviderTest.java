package it.polito.SE2.P12.SPG.jsonWebTokenTestSuite;


import com.auth0.jwt.algorithms.Algorithm;
import it.polito.SE2.P12.SPG.auth.UserDetailsImpl;
import it.polito.SE2.P12.SPG.entity.Admin;
import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import it.polito.SE2.P12.SPG.utils.DBUtilsService;
import it.polito.SE2.P12.SPG.utils.JWTProviderImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@SpringBootTest
public class JWTProviderTest {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SpgUserService spgUserService;
    @Autowired
    private DBUtilsService dbUtilsService;

    @BeforeEach
    public void initContext() {
        dbUtilsService.dropAll();
        User tester = new Admin("tester", "tester", "tester_aaaaaaaaaaaa", "", "tester@test.com", "password");
        userRepo.save(tester);
    }

    @AfterEach
    public void restDB() {
        userRepo.deleteAll();
    }

    @Test
    public void generateAccessTokenTest() throws Exception {
        JWTProviderImpl jwtProvider = new JWTProviderImpl();
        String accessToken = jwtProvider.generateAccessToken(new UserDetailsImpl(userRepo.findUserByEmail("tester@test.com")), "requestURL");
        /*Access Token generated/verified at https://jwt.io/*/
        //Check that token is composed by 3 part (header, payload, signature)
        Assertions.assertEquals(3, accessToken.split("\\.").length);
        Assertions.assertNotNull(accessToken);
        Assertions.assertNotEquals("", accessToken);
    }

    @Test
    public void generateAccessTokenTest1() {
        //null parameters
        JWTProviderImpl jwtProvider = new JWTProviderImpl();
        Assertions.assertThrows(Exception.class, () -> {
            jwtProvider.generateAccessToken(new UserDetailsImpl(userRepo.findUserByEmail("tester@test.com")), null);
        });
        Assertions.assertThrows(Exception.class, () -> {
            jwtProvider.generateAccessToken(null, "validUrl");
        });
        Assertions.assertThrows(Exception.class, () -> {
            jwtProvider.generateAccessToken(null, null);
        });
    }

    @Test
    public void generateRefreshTokenTest() throws Exception {
        JWTProviderImpl jwtProvider = new JWTProviderImpl();
        String refreshToken = jwtProvider.generateRefreshToken(new UserDetailsImpl(userRepo.findUserByEmail("tester@test.com")), "requestURL");
        /*Access Token generated at https://jwt.io/*/
        //Check that token is composed by 3 part (header, payload, signature)
        Assertions.assertEquals(3, refreshToken.split("\\.").length);
        Assertions.assertNotNull(refreshToken);
        Assertions.assertNotEquals("", refreshToken);
    }

    @Test
    public void generateRefreshTokenTest1() {
        //null parameters
        JWTProviderImpl jwtProvider = new JWTProviderImpl();
        Assertions.assertThrows(Exception.class, () -> {
            jwtProvider.generateRefreshToken(new UserDetailsImpl(userRepo.findUserByEmail("tester@test.com")), null);
        });
        Assertions.assertThrows(Exception.class, () -> {
            jwtProvider.generateRefreshToken(null, "validUrl");
        });
        Assertions.assertThrows(Exception.class, () -> {
            jwtProvider.generateRefreshToken(null, null);
        });
    }

    @Test
    public void SetGetAlgoTypeTest() {
        JWTProviderImpl jwtProvider = new JWTProviderImpl();
        Assertions.assertEquals("HmacSHA256", jwtProvider.getAlgorithmName());
        jwtProvider.setAlgorithm(Algorithm.HMAC512("Server_secret".getBytes(StandardCharsets.UTF_8)));
        Assertions.assertEquals("HmacSHA512", jwtProvider.getAlgorithmName());
    }

    @Test
    public void getFrontEndUserJWTTest() throws Exception {
        JWTProviderImpl jwtProvider = new JWTProviderImpl();
        Map<String, String> map = jwtProvider.getFrontEndUSerJWT(new UserDetailsImpl(userRepo.findUserByEmail("tester@test.com")), "requestURL");
        Assertions.assertEquals("tester@test.com", map.get("email"));
        Assertions.assertNotNull(map);
        Assertions.assertEquals("ADMIN", map.get("roles"));
        Assertions.assertNotNull(map.get("accessToken"));
        Assertions.assertNotNull(map.get("refreshToken"));
    }

    @Test
    public void verifyAccessTokenTest() throws Exception {
        JWTProviderImpl jwtProvider = new JWTProviderImpl();
        String accessToken = jwtProvider.generateAccessToken(new UserDetailsImpl(userRepo.findUserByEmail("tester@test.com")), "requestURL");
        Assertions.assertEquals("tester@test.com", jwtProvider.verifyAccessToken(accessToken));
    }

    @Test
    public void setErrorMessageTest() {
        JWTProviderImpl jwtProvider = new JWTProviderImpl();
        Exception e = new Exception("This is the error message");
        Assertions.assertEquals("This is the error message", jwtProvider.setErrorMessage(e).get("errorMessage"));
    }

    @Test
    public void verifyRefreshTokenAndRegenarateAccessTokenTest() throws Exception {
        JWTProviderImpl jwtProvider = new JWTProviderImpl();
        String refreshToken = jwtProvider.generateRefreshToken(new UserDetailsImpl(userRepo.findUserByEmail("tester@test.com")), "requestURL");
        Map<String, String> map = jwtProvider.verifyRefreshTokenAndRegenerateAccessToken(refreshToken, "requestURL", spgUserService);
        Assertions.assertNotNull(map);
        Assertions.assertEquals(refreshToken, map.get("refreshToken"));
        Assertions.assertEquals("ADMIN", map.get("roles"));
        Assertions.assertNotNull(map.get("accessToken"));
    }

    @Test
    public void verifyRefreshTokenTest() throws Exception {
        JWTProviderImpl jwtProvider = new JWTProviderImpl();
        String accessToken = jwtProvider.generateAccessToken(new UserDetailsImpl(userRepo.findUserByEmail("tester@test.com")), "requestURL");
        String username = jwtProvider.verifyAccessToken(accessToken);
        Assertions.assertEquals("tester@test.com", username);
    }

}
