package com.nezzar064.ocafe.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Embeddable
@Table(name = "dashboard_message_replies")
public class DashboardMessageReply implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "author", table = "dashboard_message_replies")
    private String author;

    @Column(name = "date", table = "dashboard_message_replies")
    private Instant date;

    @Column(name = "subject", table = "dashboard_message_replies")
    private String subject;

    @Column(name = "content", table = "dashboard_message_replies")
    private String content;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DashboardMessageReply that = (DashboardMessageReply) o;
        return Objects.equals(author, that.author) && Objects.equals(date, that.date) && Objects.equals(subject, that.subject) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, date, subject, content);
    }
}