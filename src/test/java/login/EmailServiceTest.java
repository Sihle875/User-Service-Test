package login;

import com.tradingsystem.UserService1.Login.EmailService.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void testSendEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("recipient@example.com");
        message.setSubject("Subject");
        message.setText("Body");

        emailService.sendEmail("recipient@example.com", "Subject", "Body");

        Mockito.verify(mailSender, Mockito.times(1)).send(Mockito.any(SimpleMailMessage.class));
    }
}