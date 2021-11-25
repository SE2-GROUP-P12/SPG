package it.polito.SE2.P12.SPG.utils;

import javax.persistence.*;

@Entity
public class JWTUserHandlerImpl {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private long userId;
    @Column(name = "access_token", length = 500)
    private String accessToken;
    @Column(name = "refresh_token", length = 500)
    private String refreshToken;
    @Column(name = "creation_date")
    private String creationDate;
    @Column(name = "is_valid")
    private Boolean valid;
    //Column expiration date is not required(?)


    public JWTUserHandlerImpl(long userId, String accessToken, String refreshToken, String creationDate) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.creationDate = creationDate;
        this.valid = Boolean.TRUE;
    }

    public JWTUserHandlerImpl() {
        //Do nothing...
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
}
