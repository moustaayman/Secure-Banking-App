package com.ayman.bankapp.bankingapplication.entities;

import com.ayman.bankapp.bankingapplication.enums.Gender;
import com.ayman.bankapp.bankingapplication.enums.Role;
import com.ayman.bankapp.bankingapplication.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private String city;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
    @OneToMany(mappedBy = "user")
    private List<Account> accounts = new ArrayList<>();
    //is KYC verified ?
    //private boolean isKycVerified;
}
