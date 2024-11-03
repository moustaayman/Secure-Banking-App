package com.ayman.bankapp.bankingapplication.services;

import com.ayman.bankapp.bankingapplication.dtos.EmailDetails;

public interface EmailService {
    void sendSimpleMail(EmailDetails emailDetails);
    void sendEmailWithAttachment(String toEmail, String subject, String body, byte[] bankStatement, String pdfName);
}
