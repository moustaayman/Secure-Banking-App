package com.ayman.bankapp.bankingapplication.services.ServiceImpl;

import com.ayman.bankapp.bankingapplication.dtos.EmailDetails;
import com.ayman.bankapp.bankingapplication.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
    private JavaMailSender javaMailSender;
    @Override
    public void sendSimpleMail(EmailDetails emailDetails) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(emailDetails.recipient());
            helper.setSubject(emailDetails.subject());
            helper.setText(emailDetails.body(), true);

            javaMailSender.send(message);
        } catch (MailException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEmailWithAttachment(String toEmail, String subject, String body, byte[] bankStatement, String pdfName) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body);

            helper.addAttachment(pdfName, new ByteArrayDataSource(bankStatement, "application/pdf"));

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email with attachment", e);
        }
    }


}
