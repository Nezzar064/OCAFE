package com.nezzar064.ocafe.service.mapper;

import com.nezzar064.ocafe.model.dto.DashboardDto;
import com.nezzar064.ocafe.model.dto.DashboardMessageDto;
import com.nezzar064.ocafe.model.entity.Dashboard;
import com.nezzar064.ocafe.model.entity.DashboardMessage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DashboardMessageMapper implements GenericConverter<DashboardMessageDto, DashboardMessage> {

    private ModelMapper modelMapper;

    @Autowired
    public DashboardMessageMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public DashboardMessage mapToEntity(DashboardMessageDto dto) {
        return modelMapper.map(dto, DashboardMessage.class);
    }

    @Override
    public DashboardMessageDto mapToDto(DashboardMessage entity) {
        return modelMapper.map(entity, DashboardMessageDto.class);
    }

    @Override
    public List<DashboardMessage> mapDtoListToEntityList(List<DashboardMessageDto> dtoList) {
        return dtoList.stream().map(this::mapToEntity).collect(Collectors.toList());
    }

    @Override
    public List<DashboardMessageDto> mapEntityListToDtoList(List<DashboardMessage> entityList) {
        return entityList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}
