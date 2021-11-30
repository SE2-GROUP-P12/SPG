package it.polito.SE2.P12.SPG.service;

import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.repository.JWTUserHandlerRepo;
import it.polito.SE2.P12.SPG.utils.JWTUserHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Transactional
@Service
public class JWTUserHandlerService {

    private final JWTUserHandlerRepo jwtUserHandlerRepo;


    @Autowired
    public JWTUserHandlerService(JWTUserHandlerRepo jwtUserHandlerRepo) {
        this.jwtUserHandlerRepo = jwtUserHandlerRepo;
    }

    public void addRelationUserTokens(User user, String accessToken, String refreshToken) {
        JWTUserHandlerImpl jwtUserHandlerObj = new JWTUserHandlerImpl(user.getUserId(), accessToken, refreshToken, JWTUserHandlerService.now());
        jwtUserHandlerRepo.save(jwtUserHandlerObj);
    }

    public void invalidateUserTokens(User user, String accessToken) throws Exception {
        JWTUserHandlerImpl jwtUserHandler = jwtUserHandlerRepo.findJWTUserHandlerImplByAccessToken(accessToken);
        if (jwtUserHandler.getUserId() == user.getUserId()) {
            jwtUserHandlerRepo.updateAccessTokenValidity(false, user.getUserId(), accessToken);
        } else {
            throw new Exception("ERROR: User id requested and access token bound to the access token are not consistent");
        }
    }

    public void invalidateByUserRefreshTokens(User user, String refreshToken) throws Exception {
        JWTUserHandlerImpl jwtUserHandler = jwtUserHandlerRepo.findJWTUserHandlerImplByRefreshTokenAndValidIsTrue(refreshToken);
        if (jwtUserHandler == null) {
            throw new Exception("token is not valid anymore");

        }
        if (jwtUserHandler.getUserId() == user.getUserId()) {
            jwtUserHandlerRepo.updateRefreshTokenValidity(false, user.getUserId(), refreshToken);
        } else {
            throw new Exception("ERROR: User id requested and access token bound to the refresh token are not consistent");
        }
    }

    public void checkTokenValidity(User user, String accessToken) throws Exception {
        JWTUserHandlerImpl jwtUserHandler = jwtUserHandlerRepo.findJWTUserHandlerImplByAccessToken(accessToken);
        if (jwtUserHandler.getUserId() != user.getUserId())
            throw new Exception("Token check failed: user id is wrong");
        if (!jwtUserHandler.getAccessToken().equals(accessToken))
            throw new Exception("Token check failed: token is wrong");
        if (jwtUserHandler.getValid() != Boolean.TRUE)
            throw new Exception("Token check failed: Token is not valid anymore");
    }


    private static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

}
