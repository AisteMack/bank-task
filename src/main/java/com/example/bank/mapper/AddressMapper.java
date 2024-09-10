package com.example.bank.mapper;

import com.example.bank.dto.AddressViewDto;
import com.example.bank.dto.CreateAddressDto;
import com.example.bank.entities.Address;
import com.example.bank.entities.Customer;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address map(@Nonnull CreateAddressDto address, @Nonnull Customer customer) {
        return Address.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getStreet())
                .houseNumber(address.getHouseNumber())
                .customer(customer)
                .build();
    }

    public AddressViewDto map(@Nonnull Address address) {
        return AddressViewDto.builder()
                .id(address.getId())
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getStreet())
                .houseNumber(address.getHouseNumber())
                .build();
    }
}
