package com.sccon.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PeriodOutput {
    DAYS("days"),
    MONTHS("months"),
    YEARS("years");

    private final String value;
}
