package com.sccon.presentation.person;

import com.sccon.domain.enums.SalaryOutput;
import com.sccon.infrastructure.exception.InvalidOutputException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
class StringToSalaryOutputConverter implements Converter<String, SalaryOutput> {

    @Override
    public SalaryOutput convert(String value) {
        return Stream.of(SalaryOutput.values())
            .filter(e -> e.getValue().equalsIgnoreCase(value) || e.name().equalsIgnoreCase(value))
            .findFirst()
            .orElseThrow(() -> new InvalidOutputException(
                "Invalid value for output. Accepted values: min | full"));
    }
}
