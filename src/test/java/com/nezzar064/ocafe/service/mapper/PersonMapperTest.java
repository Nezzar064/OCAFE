package com.nezzar064.ocafe.service.mapper;

import com.nezzar064.ocafe.model.dto.PersonDto;
import com.nezzar064.ocafe.model.entity.Person;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonMapperTest {

    ModelMapper mapper = new ModelMapper();
    private PersonMapper personMapper = new PersonMapper(mapper);

    @Test
    void mapToEntity() {
        Person person = new Person();
        person.setFirstName("Jens");
        person.setLastName("Derp");
        PersonDto personDto = personMapper.mapToDto(person);
        assertEquals(personDto.getFirstName(), "Jens");
        assertEquals(personDto.getLastName(), "Derp");
    }

    @Test
    void mapToDto() {
        PersonDto personDto = new PersonDto();
        personDto.setFirstName("Jens");
        personDto.setLastName("Derp");
        Person person = personMapper.mapToEntity(personDto);
        assertEquals(person.getFirstName(), "Jens");
        assertEquals(person.getLastName(), "Derp");
    }

    @Test
    void mapDtoListToEntityList() {
        PersonDto personDto1 = new PersonDto();
        personDto1.setFirstName("Jens");
        personDto1.setLastName("Derp");
        PersonDto personDto2 = new PersonDto();
        personDto2.setFirstName("Jens");
        personDto2.setLastName("Derp");
        PersonDto personDto3 = new PersonDto();
        personDto3.setFirstName("Jens");
        personDto3.setLastName("Derp");

        List<PersonDto> personDtos = new ArrayList<>();
        personDtos.add(personDto1);
        personDtos.add(personDto2);
        personDtos.add(personDto3);

        List<Person> personList = personMapper.mapDtoListToEntityList(personDtos);

        assertEquals(personList.get(0).getFirstName(), "Jens");
        assertEquals(personList.get(0).getLastName(), "Derp");

        assertEquals(personList.get(1).getFirstName(), "Jens");
        assertEquals(personList.get(1).getLastName(), "Derp");

        assertEquals(personList.get(2).getFirstName(), "Jens");
        assertEquals(personList.get(2).getLastName(), "Derp");
    }

    @Test
    void mapEntityListToDtoList() {
    }
}