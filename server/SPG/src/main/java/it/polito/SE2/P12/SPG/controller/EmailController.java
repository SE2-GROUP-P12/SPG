package it.polito.SE2.P12.SPG.controller;


import it.polito.SE2.P12.SPG.emailServer.EmailConfiguration;
import it.polito.SE2.P12.SPG.emailServer.Feedback;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.ValidationException;

@RestController
@RequestMapping("/api/feedback")
public class EmailController {

    private EmailConfiguration emailConfig;

    public EmailController(EmailConfiguration emailConfig) {
        this.emailConfig = emailConfig;
    }


    @PostMapping
    public void sendFeedback(@RequestBody Feedback feedback, BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors())
            throw new ValidationException("Feedback is not valid!");
        //create a mail sender text
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setUsername(this.emailConfig.getUsername());
        mailSender.setPassword(this.emailConfig.getPassword());
        mailSender.setHost(this.emailConfig.getHost());
        mailSender.setPort(this.emailConfig.getPort());
        //create email instance
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(feedback.getEmail());
        mailMessage.setTo("username@domain.it");
        mailMessage.setSubject("New feedback from: " + feedback.getEmail());
        mailMessage.setText(feedback.getFeedback());
        //Send email
        mailSender.send(mailMessage);
    }

}
