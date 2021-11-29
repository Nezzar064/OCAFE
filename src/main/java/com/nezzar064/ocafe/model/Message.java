package com.nezzar064.ocafe.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.nezzar064.ocafe.model.entity.CourseMessage;
import com.nezzar064.ocafe.model.entity.CourseMessageReply;
import com.nezzar064.ocafe.model.entity.DashboardMessage;
import com.nezzar064.ocafe.model.entity.DashboardMessageReply;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;
import java.time.Instant;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
/*
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DashboardMessage.class, name = "dashboard"),
        @JsonSubTypes.Type(value = DashboardMessageReply.class, name = "dashboard-reply"),
        @JsonSubTypes.Type(value = CourseMessage.class, name = "course"),
        @JsonSubTypes.Type(value = CourseMessageReply.class, name = "course-reply")
})
//Used for JSON, currently works without it.
 */
public abstract class Message {

    private String author;

    private Instant date;

    private String subject;

    private String content;
}
