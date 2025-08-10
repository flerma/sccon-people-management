package com.sccon.domain.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
public class Person {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private LocalDate hireDate;
    private BigDecimal salary;
}
