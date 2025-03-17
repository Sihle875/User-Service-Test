package SignUp.signupservice;

import com.tradingsystem.UserService1.Model.Trader;
import com.tradingsystem.UserService1.Repository.TraderRepository;
import com.tradingsystem.UserService1.SignUp.SignupService.TraderService;
import com.tradingsystem.UserService1.TraderDTO.TraderDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TraderServiceTest {

    @InjectMocks
    private TraderService traderService;

    @Mock
    private TraderRepository traderRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testCreateTrader_Success() {
        TraderDTO traderDTO = new TraderDTO();
        traderDTO.setFirstName("John");
        traderDTO.setLastName("Doe");
        traderDTO.setEmail("test@gmail.com");
        traderDTO.setPassword("Password@123");

        Trader trader = new Trader();
        trader.setId(1L);
        trader.setEmail("test@gmail.com");

        // Mock repository and encoder behaviors
        Mockito.when(traderRepository.save(Mockito.any(Trader.class))).thenReturn(trader);
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("EncodedPassword");

        TraderDTO result = traderService.createTrader(traderDTO);

        // Validate response
        Assertions.assertEquals("test@gmail.com", result.getEmail());
        Assertions.assertNotNull(result.getDateCreated());
    }

    @Test
    public void testExistsByEmail_ReturnsTrue() {
        Mockito.when(traderRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(new Trader()));

        boolean exists = traderService.existsByEmail("test@gmail.com");

        Assertions.assertTrue(exists);
    }
}

