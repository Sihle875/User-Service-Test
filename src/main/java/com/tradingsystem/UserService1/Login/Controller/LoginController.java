package com.tradingsystem.UserService1.Login.Controller;

import com.tradingsystem.UserService1.TraderDTO.TraderDTO;
import com.tradingsystem.UserService1.Login.Service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/login")

public class LoginController {
    private LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> getTraderByEmail(@Request String email, @RequestParam String password) {
        TraderDTO traderDTO = loginService.getTraderByEmail(email);
        if (traderDTO.equals(null)) {
            return new ResponseEntity<>("", HttpStatus.OK);
        }
        boolean isPasswordValid = loginService.verifyPassword(password, traderDTO.getPassword());
        if (!isPasswordValid) {
            return new ResponseEntity<>("Login unsuccessful. Please enter the correct password or reset it.", HttpStatus.OK);
        }
        return new ResponseEntity<>("User with username/ email: " + traderDTO.getEmail() + " \nsuccessfully Logged in", HttpStatus.OK);


    }

    @GetMapping("/traders")
    @CrossOrigin(origins = "http://localhost:4200") // Allow Angular frontend
    public ResponseEntity<List> getTraders(){
        return new ResponseEntity<List>(loginService.getTraders(),HttpStatus.OK);

    }

}
