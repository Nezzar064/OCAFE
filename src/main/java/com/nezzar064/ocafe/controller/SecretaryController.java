package com.nezzar064.ocafe.controller;

import com.nezzar064.ocafe.model.PersonRole;
import com.nezzar064.ocafe.model.dto.PersonDto;
import com.nezzar064.ocafe.model.dto.PersonListDto;
import com.nezzar064.ocafe.service.impl.PersonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/api")
public class SecretaryController {

    private final Logger log = LoggerFactory.getLogger(SecretaryController.class);

    private PersonServiceImpl personService;

    @Autowired
    public SecretaryController(PersonServiceImpl personService) {
        this.personService = personService;
    }

    @GetMapping({"/student-management/students"})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<PersonListDto>> getAllPersons() {
        List<PersonListDto> studentList = personService.getAllPersons(PersonRole.STUDENT);
        return new ResponseEntity<>(studentList, HttpStatus.OK);
    }

    @GetMapping({"/student-management/students/{id}", "/teacher-management/teachers/{id}"})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<PersonDto> getPersonById(@PathVariable long id) {
        PersonDto result = personService.getPersonById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/student-management/students/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<PersonDto> editStudent(@PathVariable long id, @Valid @RequestBody PersonDto student) {
        log.info("Request to update Student: {}", student);
        PersonDto result = personService.editPerson(id, student, PersonRole.STUDENT);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/student-management/students/add")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<PersonDto> createStudent(@Valid @RequestBody @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) PersonDto student) throws URISyntaxException {
        log.info("Request to create Student: {}", student);
        PersonDto result = personService.createPerson(student, PersonRole.STUDENT);
        return ResponseEntity.created(new URI("/api/student-management/students/" + result.getId())).body((result));
    }

    @DeleteMapping("/student-management/students/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") long id) {
        log.info("Request to delete Student: {}", id);
        personService.deletePerson(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/teacher-management/teachers")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<PersonListDto>> getAllTeachers() {
        List<PersonListDto> teacherList = personService.getAllPersons(PersonRole.TEACHER);
        return new ResponseEntity<>(teacherList, HttpStatus.OK);
    }

    @PutMapping("/teacher-management/teachers/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<PersonDto> editTeacher(@PathVariable long id, @Valid @RequestBody PersonDto teacher) {
        log.info("Request to update Teacher: {}", teacher);
        PersonDto result = personService.editPerson(id, teacher, PersonRole.TEACHER);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/teacher-management/teachers/add")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<PersonDto> createTeacher(@Valid @RequestBody @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) PersonDto teacher) throws URISyntaxException {
        log.info("Request to create Teacher: {}", teacher);
        PersonDto result = personService.createPerson(teacher, PersonRole.TEACHER);
        return ResponseEntity.created(new URI("/api/teacher-management/teachers/" + result.getId())).body((result));
    }

    @DeleteMapping("/teacher-management/teachers/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteTeacher(@PathVariable("id") long id) {
        log.info("Request to delete Teacher: {}", id);
        personService.deletePerson(id);
        return ResponseEntity.ok().build();
    }

}
