package com.example.bank.services;

import com.example.bank.dto.CreateAddressDto;
import com.example.bank.entities.Customer;
import com.example.bank.mapper.AddressMapper;
import com.example.bank.repositories.AddressRepository;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Transactional
    public void createAddresses(@Nonnull List<CreateAddressDto> addresses, @Nonnull Customer customer) {
        addresses.stream()
                .map(address -> addressMapper.map(address, customer))
                .forEach(addressRepository::save);
    }
}
