package com.nezzar064.ocafe.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class SubjectDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    @NotBlank
    private String name;
}
