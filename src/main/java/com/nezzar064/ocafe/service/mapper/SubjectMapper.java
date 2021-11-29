package com.nezzar064.ocafe.service.mapper;

import com.nezzar064.ocafe.model.dto.SubjectDto;
import com.nezzar064.ocafe.model.entity.Subject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubjectMapper implements GenericConverter<SubjectDto, Subject> {

    private ModelMapper modelMapper;

    @Autowired
    public SubjectMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Subject mapToEntity(SubjectDto dto) {
        return modelMapper.map(dto, Subject.class);
    }

    @Override
    public SubjectDto mapToDto(Subject entity) {
        return modelMapper.map(entity, SubjectDto.class);
    }

    @Override
    public List<Subject> mapDtoListToEntityList(List<SubjectDto> dtoList) {
        return dtoList.stream().map(this::mapToEntity).collect(Collectors.toList());
    }

    @Override
    public List<SubjectDto> mapEntityListToDtoList(List<Subject> entityList) {
        return entityList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}
