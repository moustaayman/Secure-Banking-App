package com.ayman.bankapp.bankingapplication.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountName;
    private String accountNumber;
    private BigDecimal accountBalance;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
