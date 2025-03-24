package login;

import com.tradingsystem.UserService1.Login.Controller.LoginController;
import com.tradingsystem.UserService1.TraderDTO.TraderDTO;
import com.tradingsystem.UserService1.Login.Service.LoginService;
import com.tradingsystem.UserService1.UserService1Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = UserService1Application.class)
class LoginControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    void testGetTraderByEmail_Success() throws Exception {

        // Mock data
        TraderDTO traderDTO = new TraderDTO();
        traderDTO.setEmail("test@example.com");
        traderDTO.setPassword("hashedPassword");

        // Mock service behavior
        Mockito.when(loginService.getTraderByEmail("test@example.com")).thenReturn(traderDTO);
        Mockito.when(loginService.verifyPassword("password", "hashedPassword")).thenReturn(true);

        // Perform HTTP GET request
        mockMvc.perform(get("/api/login/")
                        .param("email", "test@example.com")
                        .param("password", "password")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User with username/ email: test@example.com \nsuccessfully Logged in"));
    }

    @Test
    void testGetTraderByEmail_Failure_InvalidPassword() throws Exception {


        // Mock data
        TraderDTO traderDTO = new TraderDTO();
        traderDTO.setEmail("test@example.com");
        traderDTO.setPassword("hashedPassword");

        // Mock service behavior
        Mockito.when(loginService.getTraderByEmail("test@example.com")).thenReturn(traderDTO);
        Mockito.when(loginService.verifyPassword("wrongPassword", "hashedPassword")).thenReturn(false);

        // Perform HTTP GET request
        mockMvc.perform(get("/api/login/")
                        .param("email", "test@example.com")
                        .param("password", "wrongPassword")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Login unsuccessful. Please enter the correct password or reset it."));
    }

    @Test
    void testGetTraders_Success() throws Exception {

        // Mock data
        TraderDTO trader1 = new TraderDTO();
        trader1.setEmail("trader1@example.com");
        TraderDTO trader2 = new TraderDTO();
        trader2.setEmail("trader2@example.com");


        // Perform HTTP GET request
        mockMvc.perform(get("/api/login/traders")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}