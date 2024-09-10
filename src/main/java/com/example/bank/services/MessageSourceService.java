package com.example.bank.services;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessageSourceService {

    private final MessageSource messageSource;

    public String getMessage(@Nonnull String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}
