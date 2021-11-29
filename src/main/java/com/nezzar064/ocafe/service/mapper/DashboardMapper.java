package com.nezzar064.ocafe.service.mapper;

import com.nezzar064.ocafe.model.dto.DashboardDto;
import com.nezzar064.ocafe.model.entity.Course;
import com.nezzar064.ocafe.model.entity.Dashboard;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DashboardMapper implements GenericConverter<DashboardDto, Dashboard> {

    private ModelMapper modelMapper;

    @Autowired
    public DashboardMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Dashboard mapToEntity(DashboardDto dto) {
        return modelMapper.map(dto, Dashboard.class);
    }

    @Override
    public DashboardDto mapToDto(Dashboard entity) {
        return modelMapper.map(entity, DashboardDto.class);

    }

    @Override
    public List<Dashboard> mapDtoListToEntityList(List<DashboardDto> dtoList) {
        return dtoList.stream().map(this::mapToEntity).collect(Collectors.toList());
    }

    @Override
    public List<DashboardDto> mapEntityListToDtoList(List<Dashboard> entityList) {
        return entityList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}
