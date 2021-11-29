package com.nezzar064.ocafe.service.impl;

import com.nezzar064.ocafe.exception.DataNotFoundException;
import com.nezzar064.ocafe.model.PersonRole;
import com.nezzar064.ocafe.model.dto.CourseDto;
import com.nezzar064.ocafe.model.dto.PersonDto;
import com.nezzar064.ocafe.model.dto.PersonListDto;
import com.nezzar064.ocafe.model.dto.UserDto;
import com.nezzar064.ocafe.model.entity.Course;
import com.nezzar064.ocafe.model.entity.Person;
import com.nezzar064.ocafe.repository.PersonRepository;
import com.nezzar064.ocafe.service.interfaces.PersonService;
import com.nezzar064.ocafe.service.mapper.CourseMapper;
import com.nezzar064.ocafe.service.mapper.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PersonServiceImpl implements PersonService {

    private PersonRepository personRepository;
    private PersonMapper personMapper;
    private UserService userService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, PersonMapper personMapper, UserService userService) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
        this.userService = userService;
    }

    @Override
    public List<PersonListDto> getAllPersons(PersonRole role) {
        return personRepository.getPersonsIdNameAndLastNameFromRole(role).orElseThrow(() -> new DataNotFoundException("No persons registered in system!"));
    }

    @Override
    public PersonDto getPersonById(long id) {
        Person person = personRepository.getPersonByIdWithCourses(id).orElseThrow(() -> new DataNotFoundException("Person with id: " + id + " does not exist!"));
        PersonDto convertedPerson = personMapper.mapToDto(person);
        Set<CourseDto> courseDtoSet = new HashSet<>();
        for (Course c : person.getCourses()) {
            courseDtoSet.add(new CourseDto(c.getId(), c.getName(), c.getDepartment()));
        }
        convertedPerson.setCourses(courseDtoSet);
        convertedPerson.setUser(new UserDto(person.getUser().getUsername()));
        return convertedPerson;
    }

    @Override
    public PersonDto createPerson(PersonDto personDto, PersonRole role) {
        Person person = personMapper.mapToEntity(personDto);
        person.setRole(role);
        person.setUser(userService.createUserForPerson(personDto));
        personRepository.save(person);
        return personMapper.mapToDto(person);
    }

    @Override
    public PersonDto editPerson(long id, PersonDto personDto, PersonRole role) {
        Person person = personRepository.getByIdAndRole(id, role).orElseThrow(() -> new DataNotFoundException("Person with id: " + id + " does not exist!"));
        person.setFirstName(personDto.getFirstName());
        person.setLastName(personDto.getLastName());
        person.setBirthDate(personDto.getBirthDate());
        person.setAddress(personMapper.mapAddressToEntity(personDto.getAddress()));
        person.setPhone(personDto.getPhone());
        person.setEmail(personDto.getEmail());
        person.setRole(role);
        personRepository.save(person);
        PersonDto convertedPerson = personMapper.mapToDto(person);
        convertedPerson.setUser(new UserDto(person.getUser().getUsername()));
        return convertedPerson;
    }

    @Override
    public void deletePerson(long id) {
        Person person = personRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Person with id: " + id + " does not exist!"));
        for (Course c : person.getCourses()) {
            if (c.getPersons().size() == 1) {
                person.getCourses().remove(c);
            } else {
                c.getPersons().remove(person);
            }
        }
        personRepository.delete(person);
    }

    //Used to remove course from a list of persons, only with id's, to avoid parent deleting child
    @Transactional
    public void removeCourseFromSpecificIds(List<PersonDto> personsToRemoveCourseFrom) {
        for (int i = 0; i < personsToRemoveCourseFrom.size(); i++) {
            long personId = personsToRemoveCourseFrom.get(i).getId();
            Person person = personRepository.findById(personId).orElseThrow(() -> new DataNotFoundException("Person with id: " + personId + " does not exist!"));
            person.setCourses(null);
            personRepository.save(person);
        }
    }

    //Used for assisting Course in adding participants
    public List<Person> getPersonsFromListOfIds(List<PersonDto> personsWithIdOnly) {
        List<Person> personListWithFullInfo = new ArrayList<>();
        for (int i = 0; i < personsWithIdOnly.size(); i++) {
            long currentIndexId = personsWithIdOnly.get(i).getId();
            Person person = personRepository.findById(personsWithIdOnly.get(i).getId()).orElseThrow(() -> new DataNotFoundException("Person with id: " + currentIndexId + " does not exist!"));
            personListWithFullInfo.add(person);
        }
        return personListWithFullInfo;
    }

    public PersonDto getPersonNameFromUsername(String username) {
        return personRepository.getNameFromUsername(username).orElseThrow(() -> new DataNotFoundException("Person with username: " + username + " does not exist!"));
    }

}
