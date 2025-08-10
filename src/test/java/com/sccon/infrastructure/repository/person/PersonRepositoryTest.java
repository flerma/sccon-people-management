package com.sccon.infrastructure.repository.person;

import com.sccon.domain.model.Person;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PersonRepositoryTest {

    private final PersonRepository repository = new PersonRepository();

    @Test
    void shouldFindAllPeople() {
        List<Person> people = repository.findAll();

        assertThat(people).hasSize(3);
        assertThat(people.get(0).getName()).isEqualTo("José da Silva");
        assertThat(people.get(1).getName()).isEqualTo("Maria Oliveira");
        assertThat(people.get(2).getName()).isEqualTo("Carlos Souza");
    }

    @Test
    void shouldFindPersonById() {
        var person = repository.findById(1L);

        assertThat(person).isPresent();
        assertThat(person.get().getName()).isEqualTo("José da Silva");
        assertThat(person.get().getBirthDate()).isEqualTo(LocalDate.of(2000, 4, 6));
        assertThat(person.get().getHireDate()).isEqualTo(LocalDate.of(2020, 5, 10));
        assertThat(person.get().getSalary()).isEqualByComparingTo("1558");
    }

    @Test
    void shouldReturnEmptyWhenPersonNotFound() {
        var person = repository.findById(99L);

        assertThat(person).isEmpty();
    }

    @Test
    void shouldSaveNewPerson() {
        var newPerson = Person.builder()
            .id(4L)
            .name("Ana Santos")
            .birthDate(LocalDate.of(1995, 7, 15))
            .hireDate(LocalDate.of(2022, 1, 10))
            .salary(new BigDecimal("2500"))
            .build();

        repository.save(newPerson);

        var savedPerson = repository.findById(4L);
        assertThat(savedPerson).isPresent();
        assertThat(savedPerson.get()).usingRecursiveComparison().isEqualTo(newPerson);
    }

    @Test
    void shouldUpdateExistingPerson() {
        var existingPerson = repository.findById(1L).get();
        var updatedPerson = existingPerson.toBuilder()
            .name("José da Silva Junior")
            .salary(new BigDecimal("2000"))
            .build();

        repository.save(updatedPerson);

        var result = repository.findById(1L);
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("José da Silva Junior");
        assertThat(result.get().getSalary()).isEqualByComparingTo("2000");
    }

    @Test
    void shouldDeletePerson() {
        repository.delete(1L);

        var person = repository.findById(1L);
        assertThat(person).isEmpty();
    }

    @Test
    void shouldCheckIfPersonExists() {
        assertThat(repository.existsById(1L)).isTrue();
        assertThat(repository.existsById(99L)).isFalse();
    }

    @Test
    void shouldGetNextIdWhenRepositoryHasPeople() {
        Long nextId = repository.getNextId();

        assertThat(nextId).isEqualTo(4L);
    }

    @Test
    void shouldGetNextIdWhenRepositoryIsEmpty() {
        PersonRepository emptyRepository = new PersonRepository();

        emptyRepository.delete(1L);
        emptyRepository.delete(2L);
        emptyRepository.delete(3L);

        Long nextId = emptyRepository.getNextId();

        assertThat(nextId).isEqualTo(1L);
    }
}