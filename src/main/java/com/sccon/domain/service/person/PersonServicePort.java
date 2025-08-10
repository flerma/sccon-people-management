package com.sccon.domain.service.person;

import com.sccon.domain.enums.PeriodOutput;
import com.sccon.domain.enums.SalaryOutput;
import com.sccon.domain.model.Person;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PersonServicePort {

    public List<Person> listAll();

    public boolean existsById(Long id);

    public void save(Person person);

    public void update(Person person);

    public void partialUpdate(Person person, BigDecimal salary);

    public void deletePerson(Long id);

    public Optional<Person> findById(Long id);

    public int calculateAge(Person person, PeriodOutput output);

    public BigDecimal calculateSalary(Person person, SalaryOutput output);

    public Long getNextId();
}
