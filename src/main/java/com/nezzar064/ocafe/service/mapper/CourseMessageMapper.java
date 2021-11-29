package com.nezzar064.ocafe.service.mapper;

import com.nezzar064.ocafe.model.dto.CourseDto;
import com.nezzar064.ocafe.model.dto.CourseMessageDto;
import com.nezzar064.ocafe.model.entity.Course;
import com.nezzar064.ocafe.model.entity.CourseMessage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseMessageMapper implements GenericConverter<CourseMessageDto, CourseMessage> {

    private ModelMapper modelMapper;

    @Autowired
    public CourseMessageMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public CourseMessage mapToEntity(CourseMessageDto dto) {
        return modelMapper.map(dto, CourseMessage.class);
    }

    @Override
    public CourseMessageDto mapToDto(CourseMessage entity) {
        return modelMapper.map(entity, CourseMessageDto.class);
    }

    @Override
    public List<CourseMessage> mapDtoListToEntityList(List<CourseMessageDto> dtoList) {
        return dtoList.stream().map(this::mapToEntity).collect(Collectors.toList());
    }

    @Override
    public List<CourseMessageDto> mapEntityListToDtoList(List<CourseMessage> entityList) {
        return entityList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}
