package com.sccon.presentation.person;

import com.sccon.domain.enums.PeriodOutput;
import com.sccon.domain.enums.SalaryOutput;
import com.sccon.domain.model.Person;
import com.sccon.domain.service.person.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.BooleanUtils.isFalse;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
class PersonController {

    private final PersonService personService;

    @GetMapping
    public List<PersonResponse> listAll() {

        return personService.listAll().stream()
            .map(PersonResponse::toResponse)
            .sorted(Comparator.comparing(PersonResponse::getName))
            .toList();

    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreatePersonRequest request) {

        if (request.getId() != null && personService.existsById(request.getId()))
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        personService.save(request.toDomain());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (isFalse(personService.existsById(id)))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        personService.deletePerson(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody UpdatePersonRequest request) {

        if (isFalse(personService.existsById(id)))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        personService.update(request.toDomain(id));

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateSalary(@PathVariable Long id, @RequestParam BigDecimal salary) {

        Optional<Person> person = personService.findById(id);

        if (person.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        personService.partialUpdate(person.get(), salary);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> getById(@PathVariable Long id) {

        return personService.findById(id)
            .map(it -> ResponseEntity.ok(PersonResponse.builder()
                                                .id(it.getId())
                                                .name(it.getName())
                                                .birthDate(it.getBirthDate())
                                                .hireDate(it.getHireDate())
                                                .salary(it.getSalary())
                                                .build()))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{id}/age")
    public ResponseEntity<Integer> age(@PathVariable Long id,
                                       @Valid @RequestParam(defaultValue = "years") PeriodOutput output) {

        Optional<Person> personOpt = personService.findById(id);

        return personOpt.map(person -> ResponseEntity.ok(personService.calculateAge(person, output)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

    @GetMapping("/{id}/salary")
    public ResponseEntity<BigDecimal> salary(@PathVariable Long id,
                                             @Valid @RequestParam SalaryOutput output) {

        Optional<Person> person = personService.findById(id);

        if (person.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return person.map(value -> ResponseEntity.ok(personService.calculateSalary(value, output)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}