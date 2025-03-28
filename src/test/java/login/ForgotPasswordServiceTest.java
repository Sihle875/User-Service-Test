package login;

import com.tradingsystem.UserService1.Login.EmailService.EmailService;
import com.tradingsystem.UserService1.Login.ForgotPasswordService.ForgotPasswordService;
import com.tradingsystem.UserService1.Model.Trader;
import com.tradingsystem.UserService1.Repository.TraderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ForgotPasswordServiceTest {

    @Mock
    private TraderRepository traderRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ForgotPasswordService forgotPasswordService;

    @Test
    void testSendOtp_Success() {
        Trader trader = new Trader();
        trader.setEmail("test@example.com");

        Mockito.when(traderRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(trader));

        ResponseEntity<?> response = forgotPasswordService.sendOtp("test@example.com");

        Assertions.assertEquals("OTP sent to email.", response.getBody());
    }

    @Test
    void testSendOtp_EmailNotRegistered() {
        Mockito.when(traderRepository.findByEmail("unknown@example.com"))
                .thenReturn(Optional.empty());

        ResponseEntity<?> response = forgotPasswordService.sendOtp("unknown@example.com");

        Assertions.assertEquals("Email not registered.", response.getBody());
    }
}