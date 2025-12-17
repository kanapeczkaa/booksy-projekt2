package pl.wszib.booksyprojekt2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailService {
    
    private final JavaMailSender emailSender;
    
    
    public void sendSimpleMessage(String to, String subject, String text) {
//        emailSender.send(createSimpleMessage(to, subject, text));
        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("booksy.project2025@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
    
//    private org.springframework.mail.SimpleMailMessage createSimpleMessage(String to, String subject, String text) {
//        org.springframework.mail.SimpleMailMessage message = new org.springframework.mail.SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//        return message;
//    }
}
