package com.tradingsystem.UserService1.SignUp.SignupController;

import com.tradingsystem.UserService1.EmailVerification.EmailVerififcationService;
import com.tradingsystem.UserService1.TraderDTO.TraderDTO;
import com.tradingsystem.UserService1.SignUp.SignupService.TraderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/signUp")
public class SignUpController {
    @Autowired
    private TraderService traderService;
    @Autowired
    private EmailVerififcationService emailService;
    @Autowired
    public SignUpController(TraderService traderService, EmailVerififcationService emailService) {
        this.traderService = traderService;
        this.emailService = emailService;
    }
    TraderDTO tempTraderDTO = new TraderDTO();
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = "http://localhost:4200") // Allow Angular frontend

    public ResponseEntity<?> createdTrader(@RequestBody TraderDTO traderDTO) {
        if (!traderDTO.getPassword().equals(traderDTO.getConfirmPassword())) {
            return  ResponseEntity.ok("Passwords do not match");
            //  return  ResponseEntity.ok(HttpStatus.BAD_REQUEST);
        }
        // Validate first name (only letters, not empty)
        if (traderDTO.getFirstName() == null || traderDTO.getFirstName().trim().isEmpty()) {
            return  ResponseEntity.ok("First name is required and cannot be empty");
        }
        if (!traderDTO.getFirstName().matches("[a-zA-Z]+")) {
            return  ResponseEntity.ok("First name must contain only letters");
        }
        // Validate last name (only letters, not empty)
        if (traderDTO.getLastName() == null || traderDTO.getLastName().trim().isEmpty()) {
            return  ResponseEntity.ok("Last name is required and cannot be empty");
        }
        if (!traderDTO.getLastName().matches("[a-zA-Z]+")) {
            return  ResponseEntity.ok("Last name must contain only letters");
        } // Validate email domain
        if (traderDTO.getEmail() == null || traderDTO.getEmail().trim().isEmpty()) {
            return  ResponseEntity.ok("Email is required");
        }
        // Extract the domain part and check if it matches allowed providers
        String email = traderDTO.getEmail();
        String[] emailParts = email.split("@");
        if (emailParts.length != 2) {
            return  ResponseEntity.ok("Invalid email format");
        }
        String domain = emailParts[1].toLowerCase(); // Get the domain part
        if (!(domain.equals("gmail.com") || domain.equals("outlook.com") || domain.equals("yahoo.com") || domain.equals("icloud.com") || domain.equals("me.com") || domain.equals("geeks4learning.com") || domain.equals("mac.com"))) {
            return  ResponseEntity.ok("Invalid email format");
        }
        if (traderService.existsByEmail(email) == true) {
            return  ResponseEntity.ok("User exists!!, please Login");
        }
        // Validate password (at least 8 characters, contains uppercase, lowercase, digit, and special character)
        if (traderDTO.getPassword() == null || traderDTO.getPassword().trim().isEmpty()) {
            return  ResponseEntity.ok("Password is required");
        }
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
        if (!traderDTO.getPassword().matches(passwordRegex)) {
            return  ResponseEntity.ok("Password must be at least 8 characters long, contain an uppercase letter, a lowercase letter, a number, and a special character");
        }
        // Check if password and confirmPassword match
        if (!traderDTO.getPassword().equals(traderDTO.getConfirmPassword())) {
            return  ResponseEntity.ok("Passwords do not match");
        }
        try {
            emailService.sentOtp(email);
            tempTraderDTO = traderDTO;
            return ResponseEntity.ok().body("OTP sent to " + email);
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Failed to send OTP: " + e.getMessage());
        }
    }//validate otp and store trader in the database
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/validate-otp")
    public ResponseEntity<String> validateOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isValid = emailService.vaidateOtp(email, otp);
        if (isValid) {
            traderService.createTrader(tempTraderDTO);
            return ResponseEntity.ok("Email verified successfully!\n User with email: " + email + " successfully Registered!!!");
        } else {
            return ResponseEntity.status(400).body("Invalid or expired OTP.");
        }

    }

}
