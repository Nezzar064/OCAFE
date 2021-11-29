package com.nezzar064.ocafe.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private long id;

    private String username;

    public UserDto(String username) {
        this.username = username;
    }
}
