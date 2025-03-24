package login;

import com.tradingsystem.UserService1.Login.ForgotPasswordController.ForgotPasswordController;
import com.tradingsystem.UserService1.Login.ForgotPasswordService.ForgotPasswordService;
import com.tradingsystem.UserService1.UserService1Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = UserService1Application.class)
class ForgotPasswordControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ForgotPasswordService forgotPasswordService;

    @InjectMocks
    private ForgotPasswordController forgotPasswordController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(forgotPasswordController).build();
    }

    @Test
    void testSendOtp_Success() throws Exception {
        // Use a concrete type instead of a wildcard
        ResponseEntity<String> response = ResponseEntity.ok("OTP sent to email.");

        // Use doReturn...when syntax which can work better with generics
        Mockito.doReturn(response).when(forgotPasswordService).sendOtp("test@example.com");

        mockMvc.perform(post("/password-recovery/send-otp")
                        .param("email", "test@example.com")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("OTP sent to email."));
    }

    @Test
    void testVerifyOtp_Success() throws Exception {
        ResponseEntity<String> response = ResponseEntity.ok("OTP Verified Successfully!!!");

        Mockito.doReturn(response).when(forgotPasswordService).verifyOtp("test@example.com", "123456");

        mockMvc.perform(post("/password-recovery/verify-otp")
                        .param("email", "test@example.com")
                        .param("otp", "123456")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("OTP Verified Successfully!!!"));
    }

    @Test
    void testResetPassword_Success() throws Exception {
        ResponseEntity<String> response = ResponseEntity.ok("Password updated successfully.");

        Mockito.doReturn(response).when(forgotPasswordService).resetPassword("NewPassword123!", "NewPassword123!", "test@example.com");

        mockMvc.perform(post("/password-recovery/reset-password")
                        .param("password", "NewPassword123!")
                        .param("confirmPassword", "NewPassword123!")
                        .param("email", "test@example.com")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Password updated successfully."));
    }

    @Test
    void testResetPassword_InvalidPassword() throws Exception {
        mockMvc.perform(post("/password-recovery/reset-password")
                        .param("password", "short")
                        .param("confirmPassword", "short")
                        .param("email", "test@example.com")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Password must be at least 8 characters long, contain an uppercase letter, a lowercase letter, a number, and a special character"));
    }
}
