package com.example.Greenswamp.Services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SubscribeService {
    @Value("${spring.mail.username}")
    private  String server;
    private final JavaMailSender mailSender;

    public SubscribeService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSubscriptionEmail(String toEmail) {


        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(server);
        message.setTo(toEmail);
        message.setSubject("Thank you for subscription");
        message.setText("Hello, subscribe");

        mailSender.send(message);
    }

}
