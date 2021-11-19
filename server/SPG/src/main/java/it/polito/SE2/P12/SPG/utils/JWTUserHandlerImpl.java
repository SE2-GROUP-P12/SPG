package it.polito.SE2.P12.SPG.utils;

import javax.persistence.*;

@Entity
public class JWTUserHandlerImpl implements JWTUserHandler{

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name= "user_id", nullable = false)
    private long userId;
    @Column(name = "access_toke")
    private String accessToken;
    @Column(name = "refresh_token")
    private String refreshToken;
    @Column(name = "creation_date")
    private String creationDate;
    @Column (name = "is_valid")
    private Boolean valid;
    //Column expiration date is not required(?)


    public JWTUserHandlerImpl(long userId, String accessToken, String refreshToken, String creationDate, Boolean valid) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.creationDate = creationDate;
        this.valid = Boolean.TRUE;
    }

    public JWTUserHandlerImpl() {
        //Do nothing...
    }

    public Long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    @Override
    public void invalidateUserTokens(Long userId) {

    }


}
