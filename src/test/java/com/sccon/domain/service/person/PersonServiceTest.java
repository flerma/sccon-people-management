package com.sccon.domain.service.person;

import com.sccon.domain.enums.PeriodOutput;
import com.sccon.domain.enums.SalaryOutput;
import com.sccon.domain.model.Person;
import com.sccon.infrastructure.repository.person.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository repository;

    @InjectMocks
    private PersonService service;

    private Person joseDaSilva;

    @BeforeEach
    void setUp() {

        joseDaSilva = Person.builder()
            .id(1L)
            .name("José da Silva")
            .birthDate(LocalDate.of(2000, 4, 6))
            .hireDate(LocalDate.of(2020, 5, 10))
            .salary(new BigDecimal("1558.00"))
            .build();
    }

    @Test
    void shouldCalculateAgeInDays() {

        int age = service.calculateAge(joseDaSilva, PeriodOutput.DAYS);

        assertThat(age).isEqualTo(9125);
    }

    @Test
    void shouldCalculateAgeInMonths() {

        int age = service.calculateAge(joseDaSilva, PeriodOutput.MONTHS);

        assertThat(age).isEqualTo(300);
    }

    @Test
    void shouldCalculateAgeInYears() {

        int age = service.calculateAge(joseDaSilva, PeriodOutput.YEARS);

        assertThat(age).isEqualTo(25);
    }

    @Test
    void shouldCalculateFullSalary() {

        BigDecimal salary = service.calculateSalary(joseDaSilva, SalaryOutput.FULL);

        assertThat(salary).isEqualTo(new BigDecimal("5460.20"));
    }

    @Test
    void shouldCalculateMinimumWageSalary() {

        BigDecimal salary = service.calculateSalary(joseDaSilva, SalaryOutput.MIN);

        assertThat(salary).isEqualTo(new BigDecimal("4.19"));

    }

    @Test
    void shouldListAllPeopleSortedByName() {

        var maria = Person.builder()
            .id(2L)
            .name("Maria Silva")
            .build();

        when(repository.findAll()).thenReturn(List.of(joseDaSilva, maria));

        List<Person> result = service.listAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("José da Silva");
        assertThat(result.get(1).getName()).isEqualTo("Maria Silva");
    }

    @Test
    void shouldSavePersonWithProvidedId() {
        var person = Person.builder()
            .id(1L)
            .name("José da Silva")
            .birthDate(LocalDate.of(2000, 4, 6))
            .hireDate(LocalDate.of(2020, 5, 10))
            .salary(new BigDecimal("1558.00"))
            .build();

        service.save(person);

        verify(repository).save(any(Person.class));
        verify(repository, never()).getNextId();
    }

    @Test
    void shouldSavePersonWithGeneratedId() {
        var person = Person.builder()
            .name("José da Silva")
            .birthDate(LocalDate.of(2000, 4, 6))
            .hireDate(LocalDate.of(2020, 5, 10))
            .salary(new BigDecimal("1558.00"))
            .build();

        when(repository.getNextId()).thenReturn(1L);

        service.save(person);

        verify(repository).save(any(Person.class));
        verify(repository).getNextId();
    }

    @Test
    void shouldUpdateExistingPerson() {

        var id = 1L;
        var person = Person.builder()
            .id(id)
            .name("José da Silva")
            .birthDate(LocalDate.of(2000, 4, 6))
            .hireDate(LocalDate.of(2020, 5, 10))
            .salary(new BigDecimal("1558.00"))
            .build();

        service.update(person);

        verify(repository).save(any(Person.class));
    }

    @Test
    void shouldDeletePerson() {

        service.deletePerson(1L);

        verify(repository).delete(1L);
    }

    @Test
    void shouldFindPersonBy() {

        service.findById(1L);

        verify(repository).findById(1L);
    }

    @Test
    void shouldCheckIfPersonExists() {
        when(repository.existsById(1L)).thenReturn(true);

        boolean exists = service.existsById(1L);

        assertThat(exists).isTrue();
    }

    @Test
    void shouldPartialUpdateWhenSalaryIsNotNull() {
        BigDecimal newSalary = new BigDecimal("2000.00");

        service.partialUpdate(joseDaSilva, newSalary);

        verify(repository).save(any(Person.class));
    }

    @Test
    void shouldNotPartialUpdateWhenSalaryIsNull() {
        service.partialUpdate(joseDaSilva, null);

        verify(repository, never()).save(any(Person.class));
    }

    @Test
    void shouldGetNextId() {
        when(repository.getNextId()).thenReturn(10L);

        Long nextId = service.getNextId();

        assertThat(nextId).isEqualTo(10L);
    }
}
