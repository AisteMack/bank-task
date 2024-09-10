package com.example.bank.repositories;

import com.example.bank.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT acc FROM Account acc JOIN acc.customers cust WHERE cust.id = :customerId")
    List<Account> findAllByCustomerId(@Param("customerId") Long customerId);
}
