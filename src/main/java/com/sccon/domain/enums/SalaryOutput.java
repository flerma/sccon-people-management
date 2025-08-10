package com.sccon.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SalaryOutput {
    FULL("full"),
    MIN("min");

    private final String value;
}
