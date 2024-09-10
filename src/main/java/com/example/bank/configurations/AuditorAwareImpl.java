package com.example.bank.configurations;

import com.example.bank.exceptions.ServiceValidationException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    private final RequestContext requestContext;

    @SneakyThrows
    @Override
    public Optional<String> getCurrentAuditor() {
        String username = requestContext.getHeader("X-UserName");
        if (Objects.isNull(username)) {
            throw new ServiceValidationException("Header X-UserName is missing");
        }
        return Optional.of(username);
    }
}
