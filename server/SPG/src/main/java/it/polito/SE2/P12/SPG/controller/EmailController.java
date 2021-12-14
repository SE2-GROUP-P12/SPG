package it.polito.SE2.P12.SPG.controller;


import it.polito.SE2.P12.SPG.emailServer.EmailConfiguration;
import it.polito.SE2.P12.SPG.emailServer.Feedback;
import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import it.polito.SE2.P12.SPG.utils.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static it.polito.SE2.P12.SPG.utils.MailConstants.*;

@RestController
@RequestMapping("/api/mail")
@Slf4j
public class EmailController {

    private final EmailConfiguration emailConfig;
    private final SpgOrderService orderService;
    private final SpgUserService userService;


    public EmailController(EmailConfiguration emailConfig, SpgOrderService orderService, SpgUserService userService) {
        this.emailConfig = emailConfig;
        this.orderService = orderService;
        this.userService = userService;
    }


    @PostMapping("/solicitTopUp")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<String> sendFeedback(@RequestBody Feedback feedback, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        //Mail validity check
        if (!(checkReceiverMailValidity(feedback.getRecipient())
                &&
                checkSenderMailValidity(feedback.getSender()))) {
            return ResponseEntity.badRequest().body("Error: invalid mails");
        }
        //create a mail sender
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setUsername(this.emailConfig.getUsername());
        mailSender.setPassword(this.emailConfig.getPassword());
        mailSender.setHost(this.emailConfig.getHost());
        mailSender.setPort(this.emailConfig.getPort());
        //create email instance
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(feedback.getSender());
        mailMessage.setTo(feedback.getRecipient());
        mailMessage.setSubject(EMAIL_SUBJECT_TOP_UP_REMINDER);
        mailMessage.setText(EMAIL_MESSAGE_TOP_UP_REMINDER(feedback.getRecipient(), feedback.getSender()));
        //Send email
        mailSender.send(mailMessage); //TODO: enable to reset mail trap interaction and disable system log print
        log.info("mail from: " + feedback.getSender() + ", to: " + feedback.getRecipient());
        return ResponseEntity.ok("true");
    }

    @GetMapping("/getPendingOrdersEmail")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<Map<String, List<Long>>> getPendingOrdersMail() {
        return ResponseEntity.ok(orderService.getPendingOrdersMail());
    }

    private boolean checkSenderMailValidity(String email) {
        //Check that SENDER is present in the system repo AND (SENDER has role ShopEmployee OR an Admin)
        User user = userService.getUserByEmail(email);
        if (user == null)
            return false;
        return (userService.getUserByEmail(email).getRole().equals(UserRole.ROLE_ADMIN)
                ||
                userService.getUserByEmail(email).getRole().equals(UserRole.ROLE_SHOP_EMPLOYEE));
    }

    private boolean checkReceiverMailValidity(String email) {
        //Check that RECEIVER is present in the system repo AND (RECEIVER has role Customer OR a Farmer)
        User user = userService.getUserByEmail(email);
        if (user == null)
            return false;
        return ((userService.getUserByEmail(email).getRole().equals(UserRole.ROLE_CUSTOMER))
                ||
                userService.getUserByEmail(email).getRole().equals(UserRole.ROLE_FARMER));
    }
}
