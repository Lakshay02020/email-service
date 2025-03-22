package com.devil.email_service.service;

import org.springframework.web.multipart.MultipartFile;

public interface EmailService {
    void sendEmail(String toEmail, String text, String subject);
    void sendEmailWithAttachment(String toEmail, String text, String subject, MultipartFile attachment);
}
