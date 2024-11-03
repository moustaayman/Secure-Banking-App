package com.ayman.bankapp.bankingapplication.services.ServiceImpl;

import com.ayman.bankapp.bankingapplication.dtos.StatementResponse;
import com.ayman.bankapp.bankingapplication.dtos.TransactionHistoryResponse;
import com.ayman.bankapp.bankingapplication.entities.Account;
import com.ayman.bankapp.bankingapplication.exceptions.CustomException;
import com.ayman.bankapp.bankingapplication.repositories.AccountRepository;
import com.ayman.bankapp.bankingapplication.repositories.UserRepository;
import com.ayman.bankapp.bankingapplication.services.BankStatementService;
import com.ayman.bankapp.bankingapplication.services.EmailService;
import com.ayman.bankapp.bankingapplication.services.TransactionService;
import com.itextpdf.text.pdf.PdfPCell;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Service
@AllArgsConstructor
public class BankStatementServiceImpl implements BankStatementService {
    private AccountRepository accountRepository;
    private TransactionService transactionService;
    private  EmailService emailService;
    @Override
    public byte[] generateBankStatement(String accountNumber, LocalDate startDate, LocalDate endDate) {
        if (!accountRepository.existsByAccountNumber(accountNumber)) {
            throw new CustomException.BadRequestException("No account found with the specified account number");
        }
        StatementResponse statement = transactionService.getTransactionsByDateRange(accountNumber, startDate, endDate);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
            Paragraph title = new Paragraph("Bank Statement", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingBefore(20);
            title.setSpacingAfter(10);
            document.add(title);

            Font infoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.DARK_GRAY);
            document.add(new Paragraph("Account Number: " + statement.accountNumber(), infoFont));
            document.add(new Paragraph("Date Range: " + startDate + " to " + endDate, infoFont));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);

            Font valueFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.DARK_GRAY);
            PdfPCell dateHeader = new PdfPCell(new Phrase("Date", infoFont));
            PdfPCell typeHeader = new PdfPCell(new Phrase("Type", infoFont));
            PdfPCell amountHeader = new PdfPCell(new Phrase("Amount", infoFont));

            dateHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
            typeHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
            amountHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);

            table.addCell(dateHeader);
            table.addCell(typeHeader);
            table.addCell(amountHeader);

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (TransactionHistoryResponse transaction : statement.transactions()) {
                table.addCell(new Phrase(transaction.transactionDate().format(dateFormatter), valueFont));
                table.addCell(new Phrase(transaction.type().toString(), valueFont));
                table.addCell(new Phrase(transaction.amount().toString() + " DH", valueFont));
            }

            document.add(table);

            document.close();
            byte[] pdfBytes = outputStream.toByteArray();

            Account account = accountRepository.findByAccountNumber(accountNumber);
            String email = account.getUser().getEmail();
            emailService.sendEmailWithAttachment(
                    email,
                    "Your Bank Statement",
                    "Please find your bank statement attached for the requested period.",
                    pdfBytes,
                    "BankStatement_" + accountNumber + ".pdf"
            );

            return pdfBytes;
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF statement", e);
        }
    }
}
