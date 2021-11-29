package com.nezzar064.ocafe.service.mapper;

import com.nezzar064.ocafe.model.dto.AddressDto;
import com.nezzar064.ocafe.model.dto.CourseParticipantsDto;
import com.nezzar064.ocafe.model.dto.PersonDto;
import com.nezzar064.ocafe.model.entity.Address;
import com.nezzar064.ocafe.model.entity.Person;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersonMapper implements GenericConverter<PersonDto, Person> {

    private ModelMapper modelMapper;

    @Autowired
    public PersonMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Person mapToEntity(PersonDto dto) {
        return modelMapper.map(dto, Person.class);
    }

    @Override
    public PersonDto mapToDto(Person entity) {
        return modelMapper.map(entity, PersonDto.class);
    }

    @Override
    public List<Person> mapDtoListToEntityList(List<PersonDto> dtoList) {
        return dtoList.stream().map(this::mapToEntity).collect(Collectors.toList());
    }

    @Override
    public List<PersonDto> mapEntityListToDtoList(List<Person> entityList) {
        return entityList.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public CourseParticipantsDto convertPersonDtoToParticipantDto(PersonDto personDTO) {
        return modelMapper.map(personDTO, CourseParticipantsDto.class);
    }

    public AddressDto mapAddressToDto(Address address) {
        return modelMapper.map(address, AddressDto.class);
    }

    public Address mapAddressToEntity(AddressDto addressDto) {
        return modelMapper.map(addressDto, Address.class);
    }
}
