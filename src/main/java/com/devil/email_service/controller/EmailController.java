package com.devil.email_service.controller;

import com.devil.email_service.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class EmailController {
    @Autowired
    EmailService emailService;

    @GetMapping(value = "/ping")
    public void ping(){
        log.info("Ping received");
    }

    @PostMapping(value = "/sendEmail", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> sendMail(
            @RequestParam("toEmail") String toEmail,
            @RequestParam("text") String text,
            @RequestParam("subject") String subject,
            @RequestPart(required = false, name = "resource") MultipartFile resource) {
        try {
            log.info("Request received to send email to {} with subject {} and text {}", toEmail, text, subject);
            if (resource != null && !resource.isEmpty())
                emailService.sendEmailWithAttachment(toEmail, text, subject, resource);
            else
                emailService.sendEmail(toEmail, text, subject);
            return ResponseEntity.ok("Email sent successfully to " + toEmail);
        } catch (Exception e) {
            // Log the error for debugging purposes
            log.error("Failed to send email to {}: {}", toEmail, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email: " + e.getMessage());
        }
    }
}
