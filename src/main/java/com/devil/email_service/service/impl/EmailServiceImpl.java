package com.devil.email_service.service.impl;

import com.devil.email_service.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.devil.email_service.constants.AppConstants.SYSTEM_MAIL;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String toEmail, String text, String subject) {
        log.info(toEmail, text, subject);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SYSTEM_MAIL);
        message.setTo(toEmail);
        message.setText(text);
        message.setSubject(subject);

        javaMailSender.send(message);
        log.info("Mail sent successfully: {}", message);
    }

    @Override
    public void sendEmailWithAttachment(String toEmail, String text, String subject, MultipartFile file) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(SYSTEM_MAIL);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(text);

            // Attach file
            if (file != null && !file.isEmpty()) {
                helper.addAttachment(file.getOriginalFilename(), new ByteArrayResource(file.getBytes()));
            }

            javaMailSender.send(mimeMessage);
            log.info("Mail with attachment sent successfully to {}", toEmail);
        } catch (MessagingException | IOException e) {
            log.error("Failed to send email with attachment", e);
        }
    }
}