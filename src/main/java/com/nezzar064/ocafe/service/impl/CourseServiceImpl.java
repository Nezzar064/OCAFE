package com.nezzar064.ocafe.service.impl;

import com.nezzar064.ocafe.exception.DataNotFoundException;
import com.nezzar064.ocafe.model.dto.*;
import com.nezzar064.ocafe.model.entity.*;
import com.nezzar064.ocafe.payload.request.MessageReplyRequest;
import com.nezzar064.ocafe.payload.request.MessageRequest;
import com.nezzar064.ocafe.repository.CourseMessageRepository;
import com.nezzar064.ocafe.repository.CourseRepository;
import com.nezzar064.ocafe.service.interfaces.CourseService;
import com.nezzar064.ocafe.service.mapper.CourseMapper;
import com.nezzar064.ocafe.service.mapper.CourseMessageMapper;
import com.nezzar064.ocafe.service.mapper.PersonMapper;
import com.nezzar064.ocafe.service.mapper.SubjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CourseServiceImpl implements CourseService {

    private CourseRepository courseRepository;
    private CourseMessageRepository courseMessageRepository;
    private CourseMapper courseMapper;
    private PersonMapper personMapper;
    private SubjectMapper subjectMapper;
    private CourseMessageMapper courseMessageMapper;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, CourseMessageRepository courseMessageRepository, CourseMapper courseMapper, PersonMapper personMapper, SubjectMapper subjectMapper, CourseMessageMapper courseMessageMapper) {
        this.courseRepository = courseRepository;
        this.courseMessageRepository = courseMessageRepository;
        this.courseMapper = courseMapper;
        this.personMapper = personMapper;
        this.subjectMapper = subjectMapper;
        this.courseMessageMapper = courseMessageMapper;
    }

    //Class uses its own method, since it returns CourseListDto...
    @Override
    public List<CourseDto> findAll() {
        return null;
    }

    @Override
    public CourseDto findById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new NoResultException("Course with: " + id + " does not exist!"));
        List<PersonDto> convertedPersons = personMapper.mapEntityListToDtoList(new ArrayList<>(course.getPersons()));
        CourseDto convertedCourse = courseMapper.mapToDto(course);
        convertedCourse.setCourseParticipants(courseMapper.mapPersonDtoListToCourseParticipantDtoList(convertedPersons));
        return convertedCourse;
    }

    @Override
    @Transactional
    public CourseDto save(CourseDto courseDto) {
        Course course = new Course();
        return addOrEditCourseHelper(courseDto, course);
    }

    @Override
    @Transactional
    public CourseDto edit(CourseDto courseDto, Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Course with id: " + id + " does not exist!"));
        return addOrEditCourseHelper(courseDto, course);
    }

    @Override
    public void delete(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Course with id: " + id + " does not exist!"));
        courseRepository.delete(course);
    }


    private CourseDto addOrEditCourseHelper(CourseDto courseDto, Course course) {
        List<Subject> subjects = subjectMapper.mapDtoListToEntityList(courseDto.getSubjects());
        course.setName(courseDto.getName());
        course.setDepartment(courseDto.getDepartment());
        course.setSubjects(subjects);
        for (int i = 0; i < subjects.size(); i++) {
            subjects.get(i).setCourse(course);
        }
        courseRepository.save(course);
        return courseMapper.mapToDto(course);
    }

    @Transactional
    public CourseDto addCourseParticipants(long id, List<Person> courseParticipants) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Course with id: " + id + " does not exist!"));
        course.setPersons(new HashSet<>(courseParticipants));
        for (int i = 0; i < courseParticipants.size(); i++) {
            courseParticipants.get(i).setCourse(course);
        }
        courseRepository.save(course);
        CourseDto convertedCourse = courseMapper.mapToDto(course);
        convertedCourse.setCourseParticipants(courseMapper.mapPersonListToCourseParticipantDtoList(courseParticipants));
        return convertedCourse;
    }

    @Transactional
    public CourseDto removeCourseParticipants(List<PersonDto> personsToRemoveCourseFrom, long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Course with id: " + id + " does not exist!"));
        for (int i = 0; i < personsToRemoveCourseFrom.size(); i++) {
            course.removeCourseParticipant(personMapper.mapToEntity(personsToRemoveCourseFrom.get(i)));
        }
        courseRepository.save(course);
        return courseMapper.mapToDto(course);
    }

    public List<CourseListDto> getAllCourses() {
        return courseRepository.findAllCoursesWithIdNameAndDepartment().orElseThrow(() -> new DataNotFoundException("No courses found!"));
    }

    public CourseMessageDto addMessage(long courseId, MessageRequest courseMessageRequest, String authorName) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new DataNotFoundException("Course with id: " + courseId + " does not exist!"));
        CourseMessage courseMessage = CourseMessage.builder()
                .author(authorName)
                .subject(courseMessageRequest.getSubject())
                .content(courseMessageRequest.getContent())
                .date(Instant.now())
                .build();
        /*
        courseMessage.setAuthor(authorName);
        courseMessage.setSubject(courseMessageRequest.getSubject());
        courseMessage.setContent(courseMessageRequest.getContent());
        courseMessage.setDate(Instant.now());
         */
        course.addMessage(courseMessage);
        courseRepository.save(course);
        CourseMessage result = courseMessageRepository.getCourseMessageByDate(courseMessage.getDate()).orElseThrow(() -> new DataNotFoundException("No Course message with date: " + courseMessage.getDate() + " exists!"));
        return courseMessageMapper.mapToDto(result);
    }

    public List<CourseMessageDto> getMessagesForCourse(long courseId) {
        return courseMessageMapper.mapEntityListToDtoList(courseMessageRepository.getMessages(courseId)
        .orElseThrow(() -> new DataNotFoundException("No messages exist for course: " + courseId)));
    }

    public void deleteCourseMessageIfAuthorIsAuthor(long id, String authorName) {
        CourseMessage courseMessage = courseMessageRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Course message with id: " + id + " does not exist!"));
        if (courseMessage.getAuthor().equals(authorName)) {
            courseMessageRepository.delete(courseMessage);
        }
        else {
            throw new DataNotFoundException("Authors do not match - message could not be deleted!");
        }
    }

    public CourseMessageDto addReply(long courseMessageId, MessageReplyRequest courseMessageReplyRequest, String authorName) {
        CourseMessage courseMessage = courseMessageRepository.findById(courseMessageId).orElseThrow(() -> new DataNotFoundException("Course message with id: " + courseMessageId + " does not exist!"));
        CourseMessageReply courseMessageReply = new CourseMessageReply();
        courseMessageReply.setAuthor(authorName);
        courseMessageReply.setDate(Instant.now());
        courseMessageReply.setSubject("RE: " + courseMessage.getSubject());
        courseMessageReply.setContent(courseMessageReplyRequest.getContent());
        courseMessage.addReply(courseMessageReply);
        courseMessageRepository.save(courseMessage);
        return courseMessageMapper.mapToDto(courseMessage);
    }

}