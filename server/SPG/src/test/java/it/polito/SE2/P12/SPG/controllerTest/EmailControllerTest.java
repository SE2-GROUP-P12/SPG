package it.polito.SE2.P12.SPG.controllerTest;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import it.polito.SE2.P12.SPG.entity.Admin;
import it.polito.SE2.P12.SPG.entity.Customer;
import it.polito.SE2.P12.SPG.testSecurityConfig.SpringSecurityTestConfig;
import it.polito.SE2.P12.SPG.utils.DBUtilsService;
import it.polito.SE2.P12.SPG.utils.MailConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
    //MockMvc agent
    @Autowired
    private MockMvc mockMvc;
    //DB utils
    @Autowired
    private DBUtilsService dbUtilsService;

    //Import GreenMail extension (JUNIT-5)
    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP) //set up mail server protocol
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("duke@test.com", "password")) //remember to set-up user in test properties
            .withPerMethodLifecycle(true);//reload extension each test: ture

    @BeforeEach
    public void initContext() {
        dbUtilsService.dropAll();
        Admin admin = new Admin("duke", "duke",
                "DUKE_00000101010", "1234567890",
                "duke@test.com", "password");
        dbUtilsService.saveAdmin(admin);
        Customer customer = new Customer("customer", "customer",
                "CUST_00000101010", "1234567810",
                "customer@test.com", "password", "address");
        dbUtilsService.saveCustomer(customer);

    }

    @Test
    @WithUserDetails("tester@test.com")
    public void mailServer_SolicitTopUpTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/mail/solicitTopUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"sender\" : \"duke@test.com\"," +
                                "\"recipient\" : \"customer@test.com\"," +
                                "\"mailBody\" : \"This is the mail message\"" +
                                "}"
                        ))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals("true", result.getResponse().getContentAsString());
        MimeMessage[] receivedMessage = greenMail.getReceivedMessages();
        Assertions.assertFalse(Arrays.stream(receivedMessage).toList().isEmpty());
        //System.out.println(receivedMessage[0].getContent().toString());
        //ISSUED WITH CRLF vs LF -> check on mac-os
        Assertions.assertEquals(receivedMessage[0].getContent().toString().replaceAll("\r\n", "\n"),
                MailConstants.EMAIL_MESSAGE_TOP_UP_REMINDER("customer@test.com", "duke@test.com") + "\n");
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void mailServer_SolicitTopUpTest_ErrorHandler_BadRequest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/mail/solicitTopUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"sender\" : \"duke@test.com\"," +
                                "\"recipient\" : \"invalidUser@test.com\"," +
                                "\"mailBody\" : \"This is the mail message\"" +
                                "}"
                        ))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals("Error: invalid mails", result.getResponse().getContentAsString());
        MimeMessage[] receivedMessage = greenMail.getReceivedMessages();
        Assertions.assertEquals(0, receivedMessage.length);
    }




}
