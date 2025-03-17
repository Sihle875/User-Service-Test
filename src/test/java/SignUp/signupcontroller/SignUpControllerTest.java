package SignUp.signupcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradingsystem.UserService1.EmailVerification.EmailVerififcationService;
import com.tradingsystem.UserService1.SignUp.SignupController.SignUpController;
import com.tradingsystem.UserService1.SignUp.SignupService.TraderService;
import com.tradingsystem.UserService1.TraderDTO.TraderDTO;
import com.tradingsystem.UserService1.UserService1Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest(classes = UserService1Application.class)
public class SignUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TraderService traderService;

    @Mock
    private EmailVerififcationService emailService;

    @InjectMocks
    private SignUpController signUpController;

    @BeforeEach
    void setUp() {
        // Initialize MockMvc manually
        mockMvc = MockMvcBuilders.standaloneSetup(signUpController).build();

        // Add this line to make the user NOT exist for the create test
        Mockito.when(traderService.existsByEmail("test@gmail.com")).thenReturn(false);

        // Keep the OTP validation mock for the other test
        Mockito.when(emailService.vaidateOtp("test@gmail.com", "123456")).thenReturn(true);
    }

    //test code that creating of a trader
    @Test
    public void testCreateTrader_Success() throws Exception {
        TraderDTO traderDTO = new TraderDTO();
        traderDTO.setEmail("test@gmail.com");
        traderDTO.setFirstName("John");
        traderDTO.setLastName("Doe");
        traderDTO.setPassword("Password@123");
        traderDTO.setConfirmPassword("Password@123");

        // Mock the void method
        Mockito.doNothing().when(emailService).sentOtp("test@gmail.com");

        // Perform POST request
        mockMvc.perform(post("/api/signUp/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(traderDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("OTP sent to test@gmail.com"));
    }


    //test code that test validating an email
    @Test
    public void testValidateOtp_Success() throws Exception {
        // Mock the return value
        Mockito.when(emailService.vaidateOtp("test@gmail.com", "123456")).thenReturn(true);

        // Perform POST request
        mockMvc.perform(post("/api/signUp/validate-otp")
                        .param("email", "test@gmail.com")
                        .param("otp", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User with email: test@gmail.com successfully Registered")));
    }
}

