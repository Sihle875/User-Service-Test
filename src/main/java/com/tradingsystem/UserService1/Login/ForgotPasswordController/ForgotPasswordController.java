package com.tradingsystem.UserService1.Login.ForgotPasswordController;

import com.tradingsystem.UserService1.Login.ForgotPasswordService.ForgotPasswordService;
import com.tradingsystem.UserService1.Login.Service.LoginService;
import com.tradingsystem.UserService1.TraderDTO.TraderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/password-recovery")
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;
    private LoginService loginService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestParam String email) {

        return (forgotPasswordService.sendOtp(email));
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        return  forgotPasswordService.verifyOtp(email, otp);

    }
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String password, @RequestParam String confirmPassword,@RequestParam String email) {
        // Validate password (at least 8 characters, contains uppercase, lowercase, digit, and special character)
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            return new ResponseEntity<>("Password is required", HttpStatus.BAD_REQUEST);
        }
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
        if (!confirmPassword.matches(passwordRegex)) {
            return new ResponseEntity<>("Password must be at least 8 characters long, contain an uppercase letter, a lowercase letter, a number, and a special character", HttpStatus.BAD_REQUEST);
        }
        return forgotPasswordService.resetPassword(password, confirmPassword,email);
    }
}
