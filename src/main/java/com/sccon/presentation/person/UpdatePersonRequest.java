package com.sccon.presentation.person;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sccon.domain.model.Person;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class UpdatePersonRequest {

    @NotBlank
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("birthDay")
    private LocalDate birthDate;

    @NotNull
    @JsonProperty("hireDate")
    private LocalDate hireDate;

    @NotNull
    @JsonProperty("salary")
    private BigDecimal salary;

    Person toDomain(Long id) {
        return Person.builder()
            .id(id)
            .name(name)
            .birthDate(birthDate)
            .hireDate(hireDate)
            .salary(salary)
            .build();
    }
}
