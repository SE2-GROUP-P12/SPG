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

    public void populateDB(){
        User temp1 = new User ("Mario","Rossi","RSSMRA00D12N376V","01234567892",
                "customer ","mario.rossi@gmail.com","password");
        User temp2 = new User ("Paolo","Bianchi","BNCPLA00D12N376V","01234567892",
                "customer ","paolo.bianchi@gmail.com","password");
        User temp3 = new User ("Francesco","Conte","CNTFRN00D12N376V","01234567892",
                "employee ","francesco.conte@gmail.com", "password");
        if(userRepo.findUserByEmail("mario.rossi@gmail.com")==null)userRepo.save(temp1);
        if(userRepo.findUserByEmail("paolo.bianchi@gmail.com")==null)userRepo.save(temp2);
        if(userRepo.findUserByEmail("francesco.conte@gmail.com")==null)userRepo.save(temp3);
    }
    public Long getUserIdByEmail(String email){
        return userRepo.findUserByEmail(email).getUserId();
    }
    public User getUserByEmail(String email){
        return userRepo.findUserByEmail(email);
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
        System.out.println("Here, ssn: " + ssn + ", email : " + email);
        if (checkPresenceOfMail(email) && checkPresenceOfSSN(ssn))
            response.put("exist", false);
        else
            response.put("exist", true);
        return response;
    }

    public Boolean checkPresenceOfMail(String email) {
        return userRepo.existsByEmail(email);
    }

    private Boolean checkPresenceOfSSN(String ssn){
        return userRepo.existsBySsn(ssn);
    }

    public Map<String, String> addNewClient(User user) {
        Map<String, String> response = new HashMap<>();
        if(!userRepo.existsByEmail(user.getEmail()) || !userRepo.existsBySsn(user.getSsn())){
            response.put("responseMessage", "200-OK-(Customer already present)");
            return response;
        }
        userRepo.save(user);
        response.put("responseMessage", "200-OK-(Customer added successfully)");
        return response;
    }

}
