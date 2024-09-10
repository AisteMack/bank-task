package com.example.bank.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

@Entity
@Audited
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table
public class Account extends AuditableEntity {

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private int numberOfOwners;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ACCOUNT_CUSTOMER",
            joinColumns = @JoinColumn(name = "ACCOUNT_ID"),
            inverseJoinColumns = @JoinColumn(name = "CUSTOMER_ID")
    )
    private List<Customer> customers = new ArrayList<>();

    public void addCustomer(Customer customer) {
        if (!customers.contains(customer)) {
            customers.add(customer);
            updateNumberOfOwners();
        }
    }

    public void removeCustomer(Customer customer) {
        if (customers.remove(customer)) {
            updateNumberOfOwners();
        }
    }

    private void updateNumberOfOwners() {
        this.numberOfOwners = customers.size();
    }

}
