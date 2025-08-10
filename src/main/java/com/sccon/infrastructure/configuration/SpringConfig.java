package com.sccon.infrastructure.configuration;

import com.sccon.infrastructure.repository.person.PersonRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public PersonRepository personMemoryDataSource() {
        return new PersonRepository();
    }
}
