package com.nezzar064.ocafe.controller;

import com.nezzar064.ocafe.model.dto.CourseDto;
import com.nezzar064.ocafe.model.dto.CourseListDto;
import com.nezzar064.ocafe.model.dto.CourseMessageDto;
import com.nezzar064.ocafe.model.dto.PersonDto;
import com.nezzar064.ocafe.model.entity.Person;
import com.nezzar064.ocafe.payload.request.MessageReplyRequest;
import com.nezzar064.ocafe.payload.request.MessageRequest;
import com.nezzar064.ocafe.service.impl.CourseServiceImpl;
import com.nezzar064.ocafe.service.impl.PersonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/api/course-management")
public class CourseController {

    private final Logger log = LoggerFactory.getLogger(CourseController.class);

    private CourseServiceImpl courseService;
    private PersonServiceImpl personService;

    @Autowired
    public CourseController(CourseServiceImpl courseService, PersonServiceImpl personService) {
        this.courseService = courseService;
        this.personService = personService;
    }

    @GetMapping("/courses")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<CourseListDto>> getAllCourses() {
        log.info("Request to get all courses");
        List<CourseListDto> result = courseService.getAllCourses();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/courses/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable long id) {
        log.info("Request to get Course: {}", id);
        CourseDto result = courseService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/courses/add")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseDto courseDto) throws URISyntaxException {
        log.info("Request to create Course: {}", courseDto);
        CourseDto result = courseService.save(courseDto);
        return ResponseEntity.created(new URI("/api/course-management/courses/" + result.getId())).body((result));
    }

    @PutMapping("/courses/{id}/participants/add")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<CourseDto> addCourseParticipants(@PathVariable long id, @Valid @RequestBody List<PersonDto> courseParticipants) {
        log.info("Request to add course participants to Course: {}", courseParticipants);
        //TODO: Temporary fix, find a way to preserve the user.
        // Bug happens when converting from DTO -> Entity, userdto is null in List<PersonDto>. Might have to implement own mapper?
        List<Person> foundCourseParticipants = personService.getPersonsFromListOfIds(courseParticipants);
        CourseDto result = courseService.addCourseParticipants(id, foundCourseParticipants);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/courses/{id}/participants/remove")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<CourseDto> editCourseParticipants(@PathVariable long id, @Valid @RequestBody List<PersonDto> courseParticipantsToRemove) {
        log.info("Request to edit course participants to Course: {}", courseParticipantsToRemove);
        CourseDto result = courseService.removeCourseParticipants(courseParticipantsToRemove, id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/courses/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<CourseDto> editCourse(@PathVariable long id, @Valid @RequestBody CourseDto courseDto) {
        log.info("Request to update Course: {}", courseDto);
        CourseDto result = courseService.edit(courseDto, id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/courses/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteCourse(@PathVariable("id") long id) {
        log.info("Request to delete Course: {}", id);
        courseService.delete(id);

        return ResponseEntity.ok().build();
    }

    //TODO(Thought): Maybe this should go in under courses/id? and its own controller maybe?
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/courses/{id}/messages")
    public ResponseEntity<List<CourseMessageDto>> getMessagesForCourse(@PathVariable("id") long courseId) {
        List<CourseMessageDto> courseMessages = courseService.getMessagesForCourse(courseId);
        return new ResponseEntity<>(courseMessages, HttpStatus.OK);
    }

    @PostMapping("/courses/{id}/messages/add")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<CourseMessageDto> addMessageToCourse(@PathVariable("id") long courseId, @Valid @RequestBody MessageRequest courseMessageRequest) {
        log.info("Request to add message to Course: {}", courseMessageRequest);
        PersonDto personDto = personService.getPersonNameFromUsername(getCurrentAuthenticatedUsersUsername());
        String authorName = personDto.getFirstName() + " " + personDto.getLastName();
        CourseMessageDto result = courseService.addMessage(courseId, courseMessageRequest, authorName);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/courses/messages/{id}/reply")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<CourseMessageDto> replyToMessage(@PathVariable("id") long messageId, @Valid @RequestBody MessageReplyRequest courseMessageReplyRequest) {
        log.info("Request to add reply to Message: {}", courseMessageReplyRequest);
        PersonDto personDto = personService.getPersonNameFromUsername(getCurrentAuthenticatedUsersUsername());
        String authorName = personDto.getFirstName() + " " + personDto.getLastName();
        CourseMessageDto result = courseService.addReply(messageId, courseMessageReplyRequest, authorName);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    private String getCurrentAuthenticatedUsersUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}