package com.nezzar064.ocafe.service.interfaces;

import com.nezzar064.ocafe.model.PersonRole;
import com.nezzar064.ocafe.model.dto.PersonDto;
import com.nezzar064.ocafe.model.dto.PersonListDto;
import com.nezzar064.ocafe.model.entity.Person;

import java.util.List;

public interface PersonService {

    List<PersonListDto> getAllPersons(PersonRole role);

    PersonDto getPersonById(long id);

    PersonDto createPerson(PersonDto personDto, PersonRole role);

    PersonDto editPerson(long id, PersonDto personDto, PersonRole role);

    void deletePerson(long id);


}
