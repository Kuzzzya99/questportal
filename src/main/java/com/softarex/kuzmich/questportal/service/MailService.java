package com.softarex.kuzmich.questportal.service;

import com.softarex.kuzmich.questportal.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MailService {

    public JavaMailSender emailSender;

    @Autowired
    public MailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }


    @Value("${MY_EMAIL}")
    private String myEmail;

    @Value("${MY_PASSWORD}")
    private String myPassword;

    public String sendEmail(User user, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(myEmail);
        msg.setTo(user.getLogin());
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setText(text);
        this.emailSender.send(msg);
        return "Email Sent!";
    }

}