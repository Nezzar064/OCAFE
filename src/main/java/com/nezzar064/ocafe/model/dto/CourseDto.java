package com.nezzar064.ocafe.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nezzar064.ocafe.model.Department;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@RequiredArgsConstructor
//Used to not include null fields when displaying JSON - PersonResponse only has CourseDto w. id, name, department
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    @NotBlank
    private String name;
    @NotNull
    private Department department;
    @NotNull
    private List<SubjectDto> subjects;

    private List<CourseParticipantsDto> courseParticipants;

    public CourseDto(String name, Department department) {
        this.name = name;
        this.department = department;
    }

    public CourseDto(long id, String name, Department department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }
}