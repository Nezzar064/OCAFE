package com.nezzar064.ocafe.model.dto;

import com.nezzar064.ocafe.model.Department;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class CourseListDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private Department department;

    public CourseListDto(long id, String name, Department department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }
}
