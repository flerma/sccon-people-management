package com.sccon.domain.service.person;

import com.sccon.domain.enums.PeriodOutput;
import com.sccon.domain.enums.SalaryOutput;
import com.sccon.domain.model.Person;
import com.sccon.infrastructure.repository.person.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {

    private static final String MINIMUM_SALARY_VALUE = "1302.00";
    private static final String ANNUAL_BONUS_AMOUNT = "500.00";
    private static final String DEFAULT_PERCENTUAL_INCREASE = "0.18";

    private final PersonRepository repository;

    public List<Person> listAll() {
        return repository.findAll();
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public void save(Person person) {

        Long id = person.getId() != null ? person.getId() : getNextId();

        person = person.toBuilder().id(id).build();

        repository.save(person);
    }

    public void update(Person person) {

        repository.save(person);
    }

    public void partialUpdate(Person person, BigDecimal salary) {

        if (salary != null)
            repository.save(person.toBuilder().salary(salary).build());

    }

    public void deletePerson(Long id) {
        repository.delete(id);
    }

    public Optional<Person> findById(Long id) {
        return repository.findById(id);
    }

    public int calculateAge(Person person, PeriodOutput output) {

        var hoje = LocalDate.now();
        var periodo = Period.between(person.getBirthDate(), hoje);

        return switch (output) {
            case DAYS -> periodo.getYears() * 365;
            case MONTHS -> periodo.getYears() * 12;
            default -> periodo.getYears();
        };
    }

    public BigDecimal calculateSalary(Person person, SalaryOutput output) {

        var baseSalary = person.getSalary();
        int years = Period.between(person.getHireDate(), LocalDate.now()).getYears();
        var percentualIncrease = new BigDecimal(DEFAULT_PERCENTUAL_INCREASE);
        var annualBonus = new BigDecimal(ANNUAL_BONUS_AMOUNT);

        var salaryIncrement = baseSalary
            .multiply(percentualIncrease)
            .multiply(BigDecimal.valueOf(years));

        var totalBonus = annualBonus.multiply(BigDecimal.valueOf(years));

        var finalSalary = baseSalary
            .add(salaryIncrement)
            .add(totalBonus)
            .setScale(2, RoundingMode.CEILING);

        if (output == SalaryOutput.MIN) {
            BigDecimal minimumWage = new BigDecimal(MINIMUM_SALARY_VALUE);
            return finalSalary.divide(minimumWage, 2, RoundingMode.HALF_EVEN);
        }

        return finalSalary;
    }

    public Long getNextId() {
        return repository.getNextId();
    }
}

