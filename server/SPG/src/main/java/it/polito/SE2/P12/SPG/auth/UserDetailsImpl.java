package it.polito.SE2.P12.SPG.auth;

import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.security.ApplicationUserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class UserDetailsImpl implements UserDetails {
    private String username;
    private String password;
    private Set<SimpleGrantedAuthority> authorityList;

    public UserDetailsImpl(User user) {
        this.username = user.getEmail();
        this.password = user.getPassword();
        if (user.getRole().equals("ADMIN"))
            this.authorityList = ApplicationUserRole.ADMIN.getGrantedAuthorities();
        else if(user.getRole().equals("EMPLOYEE"))
            this.authorityList = ApplicationUserRole.EMPLOYEE.getGrantedAuthorities();
        else
            this.authorityList = ApplicationUserRole.USER.getGrantedAuthorities();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}