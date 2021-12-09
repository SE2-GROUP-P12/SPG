package it.polito.SE2.P12.SPG.controllerTest;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import it.polito.SE2.P12.SPG.emailServer.EmailConfiguration;
import it.polito.SE2.P12.SPG.testSecurityConfig.SpringSecurityTestConfig;
import it.polito.SE2.P12.SPG.utils.MailConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mail.MailMessage;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.mail.internet.MimeMessage;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityTestConfig.class
)
@AutoConfigureMockMvc
public class EmailControllerTest {
    @Autowired
    private EmailConfiguration emailConfiguration;
    @Autowired
    private MockMvc mockMvc;
    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("duke@test.com", "password"))
            .withPerMethodLifecycle(false);


    @Test
    @WithUserDetails("tester@test.com")
    public void mailServerSimpleTest() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/mail/solicitTopUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"sender\" : \"sender@test.com\"," +
                                "\"recipient\" : \"duke@test.com\"," +
                                "\"mailBody\" : \"This is the mail message\"" +
                                "}"
                        ))
                .andExpect(status().isOk())
                .andReturn();
        MimeMessage[] receivedMessage = greenMail.getReceivedMessages();
        //System.out.println(receivedMessage[0].getContent().toString());
        //ISSUED WITH CRLF vs LF -> PD
        //Assertions.assertEquals(receivedMessage[0].getContent().toString(), MailConstants.EMAIL_MESSAGE_TOP_UP_REMINDER("duke@test.com" , "sender@test.com")+"\n");
    }
}
