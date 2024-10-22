package com.ayman.bankapp.bankingapplication.dtos;

import lombok.Builder;

@Builder
public record EmailDetails(String recipient, String subject, String body, String attachment) {
}
