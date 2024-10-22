package com.ayman.bankapp.bankingapplication.services;

import com.ayman.bankapp.bankingapplication.dtos.EmailDetails;

public interface EmailService {
    void sendSimpleMail(EmailDetails emailDetails);
    void sendMailWithAttachment(EmailDetails emailDetails);
}
