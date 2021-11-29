package com.nezzar064.ocafe.model.dto;

import com.nezzar064.ocafe.model.Department;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@RequiredArgsConstructor
public class DashboardDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    @NotNull
    private Department department;

}