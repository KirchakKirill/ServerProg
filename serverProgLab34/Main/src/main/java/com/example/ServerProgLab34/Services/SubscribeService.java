package com.example.ServerProgLab34.Services;


import com.example.ServerProgLab34.Data.Subscribe;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SubscribeService {

    private final JavaMailSender mailSender;

    public SubscribeService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSubscriptionEmail(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Thank you for subscription");
        message.setText("Hello, subscribe");

        mailSender.send(message);
    }

}
