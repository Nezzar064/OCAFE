package com.nezzar064.ocafe;

import com.nezzar064.ocafe.model.dto.PersonDto;
import com.nezzar064.ocafe.model.entity.Person;
import com.nezzar064.ocafe.model.entity.User;
import com.nezzar064.ocafe.service.mapper.PersonMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

public class ModelMapperTest {

    private ModelMapper modelMapper = new ModelMapper();

    PersonMapper personMapper = new PersonMapper(modelMapper);

    @Test
    void testUserMappingsInPersonMapper() {
        User user = new User();
        user.setId(1L);
        user.setUsername("derp");

        Person person = new Person();
        person.setFirstName("herpa");
        person.setUser(user);

        PersonDto personDto = personMapper.mapToDto(person);
        assertEquals(personDto.getUser().getId(), 1L);
        assertEquals(personDto.getUser().getUsername(), "derp");

    }

}
