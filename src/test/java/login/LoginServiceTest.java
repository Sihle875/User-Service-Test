package login;

import com.tradingsystem.UserService1.Login.Service.LoginService;
import com.tradingsystem.UserService1.Model.Trader;
import com.tradingsystem.UserService1.Repository.TraderRepository;
import com.tradingsystem.UserService1.TraderDTO.TraderDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private TraderRepository traderRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginService loginService;

    @Test
    void testGetTraderByEmail_Success() {
        Trader trader = new Trader();
        trader.setEmail("test@example.com");
        trader.setPassword("encodedPassword");

        Mockito.when(traderRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(trader));

        TraderDTO traderDTO = loginService.getTraderByEmail("test@example.com");

        Assertions.assertEquals("test@example.com", traderDTO.getEmail());
    }

    @Test
    void testVerifyPassword() {
        Mockito.when(passwordEncoder.matches("rawPassword", "encodedPassword"))
                .thenReturn(true);

        boolean result = loginService.verifyPassword("rawPassword", "encodedPassword");

        Assertions.assertTrue(result);
    }
}