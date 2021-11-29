package com.nezzar064.ocafe.model.dto;

import com.nezzar064.ocafe.model.PersonRole;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class CourseParticipantsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String firstName;
    private String lastName;
    private PersonRole role;

    public CourseParticipantsDto(long id, String firstName, String lastName, PersonRole role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
}
