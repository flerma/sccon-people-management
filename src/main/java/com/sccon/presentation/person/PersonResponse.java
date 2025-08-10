package com.sccon.presentation.person;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sccon.domain.model.Person;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class PersonResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("birthDay")
    private LocalDate birthDate;

    @JsonProperty("hireDate")
    private LocalDate hireDate;

    @JsonProperty("salary")
    private BigDecimal salary;

    static PersonResponse toResponse(Person person) {
        return PersonResponse.builder()
            .id(person.getId())
            .name(person.getName())
            .birthDate(person.getBirthDate())
            .hireDate(person.getHireDate())
            .salary(person.getSalary())
            .build();
    }
}
