package com.sccon.presentation.person;

import com.sccon.domain.enums.PeriodOutput;
import com.sccon.infrastructure.exception.InvalidOutputException;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Stream;

@Component
class StringToPeriodOutputConverter implements Converter<String, PeriodOutput> {

    @Override
    public PeriodOutput convert(String value) {
        return Stream.of(PeriodOutput.values())
            .filter(enumValue -> enumValue.getValue().equalsIgnoreCase(value) ||
                enumValue.name().equalsIgnoreCase(value))
            .findFirst()
            .orElseThrow(() -> new InvalidOutputException(
                "Invalid value for output. Accepted values: days | months | years"));
    }
}

