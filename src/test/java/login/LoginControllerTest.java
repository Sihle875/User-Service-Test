package login;

import com.tradingsystem.UserService1.Login.Controller.LoginController;
import com.tradingsystem.UserService1.TraderDTO.TraderDTO;
import com.tradingsystem.UserService1.Login.Service.LoginService;
import com.tradingsystem.UserService1.UserService1Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = UserService1Application.class)
class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTraderByEmail_TraderFound_PasswordValid() {
        // Arrange
        String email = "trader@example.com";
        String password = "correctPassword";

        TraderDTO traderDTO = new TraderDTO();
        traderDTO.setEmail(email);
        traderDTO.setPassword("hashedPassword");

        when(loginService.getTraderByEmail(email)).thenReturn(traderDTO);
        when(loginService.verifyPassword(password, traderDTO.getPassword())).thenReturn(true);

        // Act
        ResponseEntity<?> response = loginController.getTraderByEmail(email, password);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("successfully Logged in"));

        // Verify interactions
        verify(loginService).getTraderByEmail(email);
        verify(loginService).verifyPassword(password, traderDTO.getPassword());
    }

    @Test
    void testGetTraderByEmail_TraderNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        String password = "anyPassword";

        when(loginService.getTraderByEmail(email)).thenReturn(null);

        // Act
        ResponseEntity<?> response = loginController.getTraderByEmail(email, password);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Trader not found", response.getBody());

        // Verify interactions
        verify(loginService).getTraderByEmail(email);
        verify(loginService, never()).verifyPassword(anyString(), anyString());
    }

    @Test
    void testGetTraderByEmail_InvalidPassword() {
        // Arrange
        String email = "trader@example.com";
        String password = "incorrectPassword";

        TraderDTO traderDTO = new TraderDTO();
        traderDTO.setEmail(email);
        traderDTO.setPassword("hashedPassword");

        when(loginService.getTraderByEmail(email)).thenReturn(traderDTO);
        when(loginService.verifyPassword(password, traderDTO.getPassword())).thenReturn(false);

        // Act
        ResponseEntity<?> response = loginController.getTraderByEmail(email, password);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login unsuccessful. Please enter the correct password or reset it.", response.getBody());

        // Verify interactions
        verify(loginService).getTraderByEmail(email);
        verify(loginService).verifyPassword(password, traderDTO.getPassword());
    }

    @Test
    void testGetTraders() {
        // Arrange
        List<TraderDTO> traders = Arrays.asList(
                new TraderDTO(),
                new TraderDTO()
        );

        when(loginService.getTraders()).thenReturn(traders);

        // Act
        ResponseEntity<List> response = loginController.getTraders();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(traders, response.getBody());

        // Verify interactions
        verify(loginService).getTraders();
    }
}