package it.polito.SE2.P12.SPG.service;

import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.CodingErrorAction;
import java.util.HashMap;
import java.util.Map;

@Service
public class SpgUserService {
    private UserRepo userRepo;

    @Autowired
    public SpgUserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Long getUserByEmail(String email){
        return userRepo.findUserByEmail(email).getUserId();
    }
    public  User getUserByUserId(Long userId){
        return userRepo.findUserByUserId(userId);
    }

    public double getWallet(String email){
        return userRepo.findUserByEmail(email).getWallet();
    }
    public double topUp(String email, double value){
        User tmp = userRepo.findUserByEmail(email);
        tmp.setWallet(tmp.getWallet()+value);
        userRepo.deleteById(tmp.getUserId());
        userRepo.save(tmp);
        return tmp.getWallet();
    }
    public Map<String, Boolean> checkPresenceOfUser(String email, String ssn){
        Map<String, Boolean> response = new HashMap<>();
        if (checkPresenceOfMail(email) && checkPresenceOfSSN(ssn))
            response.put("exist", false);
        else
            response.put("exist", true);
        return response;
    }

    private Boolean checkPresenceOfMail(String mail) {
        User tmp = userRepo.findUserByEmail(mail);
        return tmp == null;
    }

    private Boolean checkPresenceOfSSN(String ssn){
        User tmp = userRepo.findUserBySsn(ssn);
        return tmp == null;
    }

    public Map<String, String> addNewClient(User user) {
        Map<String, String> response = new HashMap<>();
        if(!checkPresenceOfMail(user.getEmail()) || !checkPresenceOfSSN(user.getSsn())){
            response.put("responseMessage", "200-OK-(Customer already present)");
            return response;
        }
        userRepo.save(user);
        response.put("responseMessage", "200-OK-(Customer added successfully)");
        return response;
    }
}
