package it.polito.SE2.P12.SPG.auth;

import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) {
        if (email == null)
            throw new UsernameNotFoundException("User not found: email is null");
        User user = userRepo.findUserByEmail(email);
        if (user == null)
            throw new UsernameNotFoundException("User not found: " + email);
        return new UserDetailsImpl(user);
    }

}
