package com.example.bank.entities;

import com.example.bank.enums.ECustomerType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Audited
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "CUSTOMER", uniqueConstraints = @UniqueConstraint(columnNames = {"NAME", "LASTNAME", "EMAIL", "phonenumber"}))
public class Customer extends AuditableEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ECustomerType customerType;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Builder.Default
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

}
