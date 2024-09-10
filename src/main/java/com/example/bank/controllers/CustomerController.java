package com.example.bank.controllers;

import com.example.bank.dto.CreateCustomerDto;
import com.example.bank.dto.CustomerViewDto;
import com.example.bank.dto.CustomersDto;
import com.example.bank.dto.UpdateCustomerDto;
import com.example.bank.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping(produces = {APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> createCustomer(@Valid @RequestBody CreateCustomerDto newCustomer) {
        return ResponseEntity.ok(customerService.createCustomer(newCustomer));
    }

    @PutMapping(value = "/{id}", produces = {APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CustomerViewDto> updateCustomer(@PathVariable("id") Long id, @Valid @RequestBody UpdateCustomerDto updatedCustomer) {
        return ResponseEntity.ok(customerService.updateCustomer(id, updatedCustomer));
    }

    @GetMapping(produces = {APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CustomersDto> getCustomers(@RequestParam(required = false) String searchTerm,
                                                     @RequestParam int page,
                                                     @RequestParam int pageSize) {
        return ResponseEntity.ok(customerService.getCustomers(searchTerm, page, pageSize));
    }
}
