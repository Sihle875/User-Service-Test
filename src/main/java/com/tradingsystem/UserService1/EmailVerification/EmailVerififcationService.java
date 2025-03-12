package com.tradingsystem.UserService1.EmailVerification;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EmailVerififcationService {
    @Autowired
    private EmailVerificationRepository repository;
    @Autowired
    private JavaMailSender mailSender;
    //IF OTP EXPIRED, THE OTP AND EMAIL REMAIN IN THE DATABASE,

    public void sentOtp(String email) throws MessagingException {
        String otp = OTPUtil.generateOTP(6);//6  numbers in otp
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);//valid for 5 minutes
        //saving OTP to database
        EmailVerification verification = repository.findByEmail(email).orElse(new EmailVerification());

        verification.setEmail(email);
        verification.setOtp(otp);
        verification.setExpirationTime(expirationTime);

        //send OTP via EMAIL
        sendEmail(email, otp);
        EmailVerificationDTO emailVerificationDTO = new EmailVerificationDTO();
        emailVerificationDTO.setOtp(verification.getOtp());
        emailVerificationDTO.setEmail(verification.getEmail());
        emailVerificationDTO.setExpirationTime(verification.getExpirationTime());
        CreateEmailVerification(emailVerificationDTO);


    }

    //message sending to email
    private void sendEmail(String email, String otp) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(email);
        helper.setSubject("YOUR OTP");
        helper.setText("Your OTP code is: " + otp + ". It is only valid for 5 minutes.");

        mailSender.send(message);

    }
    //verifying email

    public EmailVerificationDTO CreateEmailVerification(EmailVerificationDTO emailVerificationDTO) {
        //creating the object to the server
        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setOtp(emailVerificationDTO.getOtp());
        emailVerification.setEmail(emailVerificationDTO.getEmail());
        emailVerification.setExpirationTime(emailVerificationDTO.getExpirationTime());
//using the repository to save the otp ,we are sending it to the database
        repository.save(emailVerification);
        EmailVerificationDTO emailVerificationResponse = new EmailVerificationDTO();
        emailVerificationResponse.setId(emailVerification.getId());
        emailVerificationResponse.setOtp(emailVerification.getOtp());
        emailVerificationResponse.setEmail(emailVerification.getEmail());
        emailVerificationResponse.setExpirationTime(emailVerification.getExpirationTime());
        return emailVerificationResponse;


    }


    public boolean vaidateOtp(String email, String otp) {

        Optional<EmailVerification> optional = repository.findByEmail(email);
        if (optional.isPresent()) {
            EmailVerification verification = optional.get();
            if (verification.getOtp().equals(otp) && verification.getExpirationTime().isAfter(LocalDateTime.now())) {
                repository.delete(verification);
                return true;
            }
            if (verification.getExpirationTime().isAfter(LocalDateTime.now())) {
                repository.delete(verification);
            }


        }
        return false;

    }

    //MAPPING THE DTO TO THE ENTITY
//    public EmailVerificationDTO mapToDTO(EmailVerification emailVerification){
//        EmailVerificationDTO emailVerificationDTO=new EmailVerificationDTO();
//        emailVerificationDTO.setId(emailVerification.getId());
//        emailVerificationDTO.setEmail(emailVerification.getEmail());
//        emailVerificationDTO.setOtp(emailVerification.getOtp());
//        emailVerificationDTO.setExpirationTime(emailVerification.getExpirationTime());
//        return  emailVerificationDTO;
//
//    }
    //MAPPING THE ENTITY TO THE DTO
//    public  EmailVerification mapToEntity(EmailVerificationDTO emailVerificationDTO){
//        EmailVerification emailVerification=new EmailVerification();
//        emailVerification.setId(emailVerificationDTO.getId());
//        emailVerification.setOtp(emailVerificationDTO.getOtp());
//        emailVerification.setEmail(emailVerificationDTO.getEmail());
//        emailVerification.setExpirationTime(emailVerificationDTO.getExpirationTime());
//        return emailVerification;

    //    }
    // Check if email is verified
    public boolean isEmailVerified(String email) {
        Optional<EmailVerification> optional = repository.findByEmail(email);
        return optional.isEmpty(); // Email is verified if it no longer exists in the repository
    }
    // Scheduled cleanup of expired OTPs
    @Scheduled(fixedRate = 60000) // Runs every minute
    @Transactional

    public void cleanupExpiredOtps() {
        LocalDateTime now = LocalDateTime.now();
        repository.deleteByExpirationTimeBefore(now);
    }

}
