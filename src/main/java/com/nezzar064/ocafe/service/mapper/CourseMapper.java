package com.nezzar064.ocafe.service.mapper;

import com.nezzar064.ocafe.model.dto.CourseDto;
import com.nezzar064.ocafe.model.dto.CourseParticipantsDto;
import com.nezzar064.ocafe.model.dto.PersonDto;
import com.nezzar064.ocafe.model.entity.Course;
import com.nezzar064.ocafe.model.entity.Person;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseMapper implements GenericConverter<CourseDto, Course> {

    private ModelMapper modelMapper;

    @Autowired
    public CourseMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Course mapToEntity(CourseDto dto) {
        return modelMapper.map(dto, Course.class);
    }

    @Override
    public CourseDto mapToDto(Course entity) {
        return modelMapper.map(entity, CourseDto.class);
    }

    @Override
    public List<Course> mapDtoListToEntityList(List<CourseDto> dtoList) {
        return dtoList.stream().map(this::mapToEntity).collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> mapEntityListToDtoList(List<Course> entityList) {
        return entityList.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<CourseParticipantsDto> mapPersonDtoListToCourseParticipantDtoList(List<PersonDto> personDtos) {
        return personDtos.stream().map(this::convertPersonDtoToParticipantDto).collect(Collectors.toList());
    }

    public List<CourseParticipantsDto> mapPersonListToCourseParticipantDtoList(List<Person> persons) {
        return persons.stream().map(this::convertPersonToParticipantDto).collect(Collectors.toList());
    }

    //Helper method
    private CourseParticipantsDto convertPersonDtoToParticipantDto(PersonDto personDTO) {
        return modelMapper.map(personDTO, CourseParticipantsDto.class);
    }

    //Helper method
    private CourseParticipantsDto convertPersonToParticipantDto(Person person) {
        return modelMapper.map(person, CourseParticipantsDto.class);
    }

}
