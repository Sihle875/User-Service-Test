package com.tradingsystem.UserService1.Login.ForgotPasswordService;

import com.tradingsystem.UserService1.Login.EmailService.EmailService;
import com.tradingsystem.UserService1.Model.Trader;
import com.tradingsystem.UserService1.Repository.TraderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service

public class ForgotPasswordService {

    private final TraderRepository traderRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public ForgotPasswordService(TraderRepository traderRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.traderRepository = traderRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    // Temporary storage for OTPs
    private final Map<String, String> otpStorage = new HashMap<>();

    public ResponseEntity<?> sendOtp(String email) {
        Optional<Trader> traderOptional = traderRepository.findByEmail(email);
        if (traderOptional.isEmpty()) {
            return ResponseEntity.ok().body("Email not registered.");
        }
        String otp = generateOtp();
        otpStorage.put(email, otp);
        emailService.sendEmail(email, "Password Recovery OTP", "Your OTP is: " + otp);
        return ResponseEntity.ok().body( "OTP sent to email.");
    }

    public ResponseEntity<?> verifyOtp(String email, String otp) {
        if (otpStorage.containsKey(email) && otpStorage.get(email).equals(otp)) {
            otpStorage.remove(email);
            return ResponseEntity.ok().body("OTP Verified Successfully!!!");
        } else {
            return ResponseEntity.ok().body("Invalid or expired OTP.");
        }
    }

    public ResponseEntity<?>  resetPassword(String password, String ConfirmPassword,String email) {
        Optional<Trader> traderOptional = traderRepository.findByEmail(email);
//        if (traderOptional.isEmpty()) {
//            return ResponseEntity.ok().body("Email not registered.");
//        }

        Trader trader = traderOptional.get();
        //hashing password before saving it to the database
        String hashedPassword=passwordEncoder.encode(ConfirmPassword);

        trader.setPassword(hashedPassword); // Add hashing here for security
        traderRepository.save(trader);
        return ResponseEntity.ok().body("Password updated successfully.");
    }

    private String generateOtp() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}
