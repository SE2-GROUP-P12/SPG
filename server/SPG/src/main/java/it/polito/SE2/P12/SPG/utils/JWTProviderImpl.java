package it.polito.SE2.P12.SPG.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import it.polito.SE2.P12.SPG.auth.UserDetailsImpl;
import it.polito.SE2.P12.SPG.service.SpgUserService;
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
        responseBody.put("roles", userDetails.getAuthorities().toString());
        responseBody.put("email", username);
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
        String[] roles = jwtDecoded.getClaim("roles").asArray(String.class);
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
                .withExpiresAt(new Date(System.currentTimeMillis() + Constants.EXPIRED_INTERVAL_TIME_ACCESS_TOKEN))
                .withIssuer(requestURL)
                .withClaim("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
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
                .withExpiresAt(new Date(System.currentTimeMillis() + Constants.EXPIRED_INTERVAL_TIME_REFRESH_TOKEN))
                .withIssuer(requestURL)
                .sign(algorithm);
    }

    @Override
    public Map<String, String> getFrontEndUSerJWT(UserDetailsImpl userDetails, String requestURL) throws Exception {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("accessToken", generateAccessToken(userDetails, requestURL));
        responseBody.put("refreshToken", generateRefreshToken(userDetails, requestURL));
        responseBody.put("roles", userDetails.getAuthorities().toString()); //Max 1 authority(?)
        responseBody.put("email", userDetails.getUsername());
        return responseBody;
    }

    @Override
    public void setAlgorithm(Algorithm newAlgo) {
        this.algorithm = newAlgo;
    }


}
