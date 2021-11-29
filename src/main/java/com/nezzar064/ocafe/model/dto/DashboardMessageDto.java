package com.nezzar064.ocafe.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@RequiredArgsConstructor
public class DashboardMessageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;

    private String author;

    @JsonFormat(pattern = "dd-MM-yyyy @ HH:mm", timezone = "UTC")
    private Instant date;

    private String subject;

    private String content;

    private List<DashboardMessageReplyDto> replies;


}