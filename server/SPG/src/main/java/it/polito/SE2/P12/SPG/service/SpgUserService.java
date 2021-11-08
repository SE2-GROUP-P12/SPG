package it.polito.SE2.P12.SPG.service;

import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SpgUserService {
    private UserRepo userRepo;

    @Autowired
    public SpgUserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    public Map<String, Boolean> checkPresenceOfMail(String mail) {
        Map<String, Boolean> response = new HashMap<>();
        User tmp = userRepo.findUserByEmail(mail);
        System.out.println("Here" + tmp);
        if (tmp==null)
            response.put("exist", false);
        else
            response.put("exist", true);
        return response;
    }

    public Map<String, String> addNewClient(User user) {
        Map<String, String> response = new HashMap<>();
        userRepo.save(user);
        response.put("responseStatus", "200-OK");
        return response;
    }
}
