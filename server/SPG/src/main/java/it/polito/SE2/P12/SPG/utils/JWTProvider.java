package it.polito.SE2.P12.SPG.utils;

import com.auth0.jwt.algorithms.Algorithm;
import it.polito.SE2.P12.SPG.auth.UserDetailsImpl;
import it.polito.SE2.P12.SPG.service.SpgUserService;

import java.util.Map;

public interface JWTProvider {

    public String generateAccessToken(UserDetailsImpl userDetails, String requestURL);

    public String generateRefreshToken(UserDetailsImpl userDetails, String requestURL);

    public Map<String, String> getFrontEndUSerJWT(UserDetailsImpl userDetails, String requestURL);

    public void setAlgorithm(Algorithm newAlgo); //Default is HMAC-256

    public String verifyAccessToken(String token);

    public Map<String, String> setErrorMessage(Exception e);

    public Map<String, String> verifyRefreshTokenAndRegenerateAccessToken(String refreshToken, String requestURL, SpgUserService userService);

}
