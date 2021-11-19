package it.polito.SE2.P12.SPG.utils;

import it.polito.SE2.P12.SPG.entity.User;

public interface JWTUserHandler {
    public void invalidateUserTokens(Long userId);
}
