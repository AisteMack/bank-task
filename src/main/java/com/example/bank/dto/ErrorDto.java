package com.example.bank.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ErrorDto {

    private final String message;

    private final List<String> details;

}
