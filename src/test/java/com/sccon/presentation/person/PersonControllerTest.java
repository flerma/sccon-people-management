package com.sccon.presentation.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sccon.domain.enums.PeriodOutput;
import com.sccon.domain.enums.SalaryOutput;
import com.sccon.domain.model.Person;
import com.sccon.domain.service.person.PersonServicePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonServicePort service;

    @Test
    void shouldListAllPeople() throws Exception {

        var people = Stream.of(
            Person.builder()
                .id(1L)
                .name("José")
                .birthDate(LocalDate.of(1990, 1, 1))
                .hireDate(LocalDate.of(2020, 1, 1))
                .build(),
            Person.builder()
                .id(2L)
                .name("Fernando")
                .birthDate(LocalDate.of(1987, 1, 1))
                .hireDate(LocalDate.of(2021, 1, 1))
                .build()
        ).sorted(Comparator.comparing(Person::getName)).toList();

        when(service.listAll()).thenReturn(people);

        mockMvc.perform(get("/person"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldCreatePerson() throws Exception {

        var request = CreatePersonRequest.builder()
            .name("José")
            .birthDate(LocalDate.of(1990, 1, 1))
            .hireDate(LocalDate.of(2020, 1, 1))
            .salary(new BigDecimal("2000.00"))
            .build();

        when(service.existsById(any())).thenReturn(false);

        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());
    }

    @Test
    void shouldCreatePersonWhenIdExistsButNotFound() throws Exception {

        var request = CreatePersonRequest.builder()
            .id(1L)
            .name("José")
            .birthDate(LocalDate.of(1990, 1, 1))
            .hireDate(LocalDate.of(2020, 1, 1))
            .salary(new BigDecimal("2000.00"))
            .build();

        var person = request.toDomain();

        when(service.existsById(1L)).thenReturn(false);

        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());

        verify(service).existsById(1L);
        verify(service).save(person);
    }

    @Test
    void shouldNotCreatePersonWhenIdAlreadyExists() throws Exception {

        var request = CreatePersonRequest.builder()
            .id(1L)
            .name("José")
            .birthDate(LocalDate.of(1990, 1, 1))
            .hireDate(LocalDate.of(2020, 1, 1))
            .salary(new BigDecimal("2000.00"))
            .build();

        when(service.existsById(1L)).thenReturn(true);

        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict());

        verify(service, never()).save(any());
    }

    @Test
    void shouldDeletePerson() throws Exception {

        when(service.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/person/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void shouldNotDeleteWhenPersonNotFound() throws Exception {

        when(service.existsById(1L)).thenReturn(false);

        mockMvc.perform(delete("/person/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdatePerson() throws Exception {

        var request = UpdatePersonRequest.builder()
            .name("José")
            .birthDate(LocalDate.of(1990, 1, 1))
            .hireDate(LocalDate.of(2020, 1, 1))
            .salary(new BigDecimal("2000.00"))
            .build();

        when(service.existsById(1L)).thenReturn(true);

        mockMvc.perform(put("/person/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentPerson() throws Exception {

        var request = UpdatePersonRequest.builder()
            .name("José")
            .birthDate(LocalDate.of(1990, 1, 1))
            .hireDate(LocalDate.of(2020, 1, 1))
            .salary(new BigDecimal("2000.00"))
            .build();

        when(service.existsById(99L)).thenReturn(false);

        mockMvc.perform(put("/person/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());

        verify(service, never()).update(any());
    }

    @Test
    void shouldUpdateSalary() throws Exception {

        var person = Person.builder()
            .id(1L)
            .name("José")
            .salary(new BigDecimal("2000.00"))
            .build();

        when(service.findById(1L)).thenReturn(Optional.of(person));

        mockMvc.perform(patch("/person/1")
                .param("salary", "2500.00"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingSalaryForNonExistentPerson() throws Exception {
        when(service.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/person/99")
                .param("salary", "2500.00"))
            .andExpect(status().isNotFound());

        verify(service, never()).partialUpdate(any(), any());
    }

    @Test
    void shouldGetPersonById() throws Exception {

        var person = Person.builder()
            .id(1L)
            .name("José")
            .birthDate(LocalDate.of(1990, 1, 1))
            .hireDate(LocalDate.of(2020, 1, 1))
            .salary(new BigDecimal("2000.00"))
            .build();

        when(service.findById(1L)).thenReturn(Optional.of(person));

        mockMvc.perform(get("/person/1"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldCalculateAge() throws Exception {

        var person = Person.builder()
            .id(1L)
            .birthDate(LocalDate.of(1990, 1, 1))
            .build();

        when(service.findById(1L)).thenReturn(Optional.of(person));
        when(service.calculateAge(person, PeriodOutput.YEARS)).thenReturn(33);

        mockMvc.perform(get("/person/1/age"))
            .andExpect(status().isOk())
            .andExpect(content().string("33"));
    }

    @Test
    void shouldCalculateSalary() throws Exception {

        var person = Person.builder()
            .id(1L)
            .salary(new BigDecimal("2000.00"))
            .build();

        when(service.findById(1L)).thenReturn(Optional.of(person));
        when(service.calculateSalary(person, SalaryOutput.FULL))
            .thenReturn(new BigDecimal("2500.00"));

        mockMvc.perform(get("/person/1/salary")
                .param("output", "FULL"))
            .andExpect(status().isOk())
            .andExpect(content().string("2500.00"));
    }

    @Test
    void shouldReturnNotFoundWhenCalculatingSalaryForNonExistentPerson() throws Exception {

        when(service.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/person/99/salary")
                .param("output", "FULL"))
            .andExpect(status().isNotFound());

        verify(service, never()).calculateSalary(any(), any());
    }


    @Test
    void shouldReturnNotFoundForNonExistentPerson() throws Exception {

        when(service.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/person/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldValidateCreateRequest() throws Exception {

        var request = CreatePersonRequest.builder().build();

        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldValidateUpdateRequest() throws Exception {

        var request = UpdatePersonRequest.builder().build();

        mockMvc.perform(put("/person/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
}