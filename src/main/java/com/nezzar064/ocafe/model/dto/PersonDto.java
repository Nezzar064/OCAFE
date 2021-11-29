package com.nezzar064.ocafe.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nezzar064.ocafe.model.PersonRole;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    @NotBlank(message = "Please provide a valid first name!")
    private String firstName;
    @NotBlank(message = "Please provide a valid first name!")
    private String lastName;
    @NotNull(message = "Please provide a valid date!")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    @NotBlank(message = "Please provide a valid phone number!")
    @Length(max = 15, message = "Please provide a valid phone number! (max 15 char)")
    private String phone;
    @NotBlank(message = "Please provide a valid email!")
    private String email;
    @NotNull(message = "Please provide a valid role!")
    private PersonRole role;
    @NotNull(message = "Please provide a valid address!")
    private AddressDto address;

    private Set<CourseDto> courses;
    private UserDto user;

    public PersonDto(long id, String firstName, String lastName, PersonRole role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public PersonDto(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public PersonDto(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
