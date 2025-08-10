package com.sccon.infrastructure.repository.person;

import com.sccon.domain.model.Person;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PersonRepository {

    private final Map<Long, Person> people = new ConcurrentHashMap<>();

    public PersonRepository() {

        people.put(1L, Person.builder()
            .id(1L)
            .name("José da Silva")
            .birthDate(LocalDate.of(2000, 4, 6))
            .hireDate(LocalDate.of(2020, 5, 10))
            .salary(BigDecimal.valueOf(1558))
            .build());

        people.put(2L, Person.builder()
            .id(2L)
            .name("Maria Oliveira")
            .birthDate(LocalDate.of(1990, 8, 12))
            .hireDate(LocalDate.of(2015, 1, 5))
            .salary(BigDecimal.valueOf(2100))
            .build());

        people.put(3L, Person.builder()
            .id(3L)
            .name("Carlos Souza")
            .birthDate(LocalDate.of(1985, 11, 30))
            .hireDate(LocalDate.of(2010, 3, 20))
            .salary(BigDecimal.valueOf(1987))
            .build());
    }

    public List<Person> findAll() {
        return new ArrayList<>(people.values());
    }

    public Optional<Person> findById(Long id) {
        return Optional.ofNullable(people.get(id));
    }

    public void save(Person person) {
        people.put(person.getId(), person);
    }

    public void delete(Long id) {
        people.remove(id);
    }

    public boolean existsById(Long id) {
        return people.containsKey(id);
    }

    public Long getNextId() {
        return people.keySet().stream().max(Long::compare).orElse(0L) + 1;
    }
}
