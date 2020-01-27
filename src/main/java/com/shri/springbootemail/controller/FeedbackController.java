package com.shri.springbootemail.controller;

import com.shri.springbootemail.config.EmailConfig;
import com.shri.springbootemail.model.Feedback;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ValidationException;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final EmailConfig emailConfig;

    public FeedbackController(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    @PostMapping
    public void sendFeedback(@RequestBody Feedback feedback, BindingResult result) {
        if(result.hasErrors()) {
            throw new ValidationException("Invalid feedback");
        }

        // 1. create the mail sender
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailConfig.getHost());
        mailSender.setPort(emailConfig.getPort());
        mailSender.setUsername(emailConfig.getUsername());
        mailSender.setPassword(emailConfig.getPassword());

        // 2. create a mail instance
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(feedback.getEmail());
        message.setTo("zeeroiq@email.com");
        message.setSubject("New feedback from " + feedback.getName());
        message.setText(feedback.getFeedback());

        // 3. send mail
        mailSender.send(message);
    }
}
