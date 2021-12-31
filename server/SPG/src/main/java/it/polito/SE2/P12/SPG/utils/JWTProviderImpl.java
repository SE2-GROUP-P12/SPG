package it.polito.SE2.P12.SPG.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import it.polito.SE2.P12.SPG.auth.UserDetailsImpl;
import it.polito.SE2.P12.SPG.repository.CustomerRepo;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import it.polito.SE2.P12.SPG.security.ApplicationUserRole;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import it.polito.SE2.P12.SPG.utils.Constants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class JWTProviderImpl implements JWTProvider {


    //TODO: maybe with ECDSA it's better -> non-repudiation
    private Algorithm algorithm = Algorithm.HMAC256(Constants.SERVER_SECRET.getBytes(StandardCharsets.UTF_8));
    private static Integer expirationAccessToken = Constants.EXPIRED_INTERVAL_TIME_ACCESS_TOKEN;
    private static Integer expirationRefreshToken = Constants.EXPIRED_INTERVAL_TIME_REFRESH_TOKEN;

    public static void setExpirationAccessToken(Integer newValue) {
        expirationAccessToken = newValue;
    }

    public static void setExpirationRefreshToken(Integer newValue) {
        expirationAccessToken = newValue;
    }

    public String getAlgorithmName() {
        return algorithm.toString();
    }

    public JWTProviderImpl() {
        //Do nothing...
    }

    private String verifyRefreshToken(String refreshToken) {
        JWTVerifier jwtVerifier = JWT.require(this.algorithm).build();
        DecodedJWT jwtDecoded = jwtVerifier.verify(refreshToken);
        return jwtDecoded.getSubject();
    }

    @Override
    public Map<String, String> verifyRefreshTokenAndRegenerateAccessToken(String refreshToken, String requestURL, SpgUserService userService) throws Exception {
        Map<String, String> responseBody = new HashMap<>();
        String username = this.verifyRefreshToken(refreshToken); //username as email
        UserDetailsImpl userDetails = new UserDetailsImpl(userService.getUserByEmail(username));
        String accessToken = this.generateAccessToken(userDetails, requestURL);
        responseBody.put("accessToken", accessToken);
        responseBody.put("refreshToken", refreshToken);
        responseBody.put(Constants.JSON_ROLES, userDetails.getAuthorities().toString().split("_")[1].replace("]", ""));
        responseBody.put(Constants.JSON_EMAIL, username);
        if (responseBody.get(Constants.JSON_ROLES).equals(ApplicationUserRole.CUSTOMER.name()))
            responseBody.put("MissedPickUp", String.valueOf(userDetails.getMissedPickUp()));
        return responseBody;
    }

    @Override
    public Map<String, String> setErrorMessage(Exception e) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("errorMessage", e.getMessage());
        return responseBody;
    }

    @Override
    public String verifyAccessToken(String token) {
        JWTVerifier jwtVerifier = JWT.require(this.algorithm).build();
        DecodedJWT jwtDecoded = jwtVerifier.verify(token);
        String username = jwtDecoded.getSubject();
        String[] roles = jwtDecoded.getClaim(Constants.JSON_ROLES).asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return username;
    }

    @Override
    public String generateAccessToken(UserDetailsImpl userDetails, String requestURL) throws Exception {
        if (requestURL == null || requestURL.equals(""))
            throw new Exception("Invalid URL");
        if (userDetails == null)
            throw new Exception("Invalid userDetails");
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationAccessToken))
                .withIssuer(requestURL)
                .withClaim(Constants.JSON_ROLES, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .sign(algorithm);
    }

    @Override
    public String generateRefreshToken(UserDetailsImpl userDetails, String requestURL) throws Exception {
        if (requestURL == null || requestURL.equals(""))
            throw new Exception("Invalid URL");
        if (userDetails == null)
            throw new Exception("Invalid userDetails");
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationRefreshToken))
                .withIssuer(requestURL)
                .sign(algorithm);
    }


    @Override
    public Map<String, String> getFrontEndUSerJWT(UserDetailsImpl userDetails, String requestURL) throws Exception {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("accessToken", generateAccessToken(userDetails, requestURL));
        responseBody.put("refreshToken", generateRefreshToken(userDetails, requestURL));
        responseBody.put(Constants.JSON_ROLES, userDetails.getAuthorities().toString().split("_")[1].replace("]", "")); //Max 1 authority(?)
        responseBody.put(Constants.JSON_EMAIL, userDetails.getUsername());
        if (responseBody.get(Constants.JSON_ROLES).equals(ApplicationUserRole.CUSTOMER.name()))
            responseBody.put("MissedPickUp", String.valueOf(userDetails.getMissedPickUp()));
        return responseBody;
    }

    @Override
    public void setAlgorithm(Algorithm newAlgo) {
        this.algorithm = newAlgo;
    }


}
