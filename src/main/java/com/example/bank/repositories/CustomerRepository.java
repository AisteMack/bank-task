package com.example.bank.repositories;

import com.example.bank.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findCustomerByNameAndLastNameAndPhoneNumberAndEmail(String customerName, String lastName,
                                                                           String phoneNumber, String email);

    @Query("SELECT cust " +
            "FROM Customer cust " +
            "JOIN cust.addresses adr " +
            "WHERE " +
            "LOWER(cust.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(cust.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(cust.customerType) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(adr.street) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(adr.city) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Customer> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
}
