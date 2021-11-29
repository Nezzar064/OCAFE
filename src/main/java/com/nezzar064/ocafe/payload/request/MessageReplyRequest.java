package com.nezzar064.ocafe.payload.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
public class MessageReplyRequest {

    @NotBlank
    private String content;
}
