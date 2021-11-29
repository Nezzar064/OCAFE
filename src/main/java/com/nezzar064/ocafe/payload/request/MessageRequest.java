package com.nezzar064.ocafe.payload.request;

import com.nezzar064.ocafe.model.Department;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class MessageRequest {

    @NotBlank
    private String subject;

    @NotBlank
    private String content;
}
