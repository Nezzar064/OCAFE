package com.nezzar064.ocafe.model.entity;

import com.nezzar064.ocafe.model.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "course_messages")
@SuperBuilder
public class CourseMessage extends Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_msg_seq_gen")
    @SequenceGenerator(name = "course_msg_seq_gen", sequenceName = "course_msg_seq_gen", allocationSize = 1)
    private long id;

    @ElementCollection
    @CollectionTable(name = "course_message_replies", joinColumns = @JoinColumn(name = "course_message_id"))
    private List<CourseMessageReply> courseMessageReplies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @ToString.Exclude
    private Course course;

    public void addReply(CourseMessageReply courseMessageReply) {
        if (this.courseMessageReplies == null) {
            courseMessageReplies = new ArrayList<>();
        }
        this.courseMessageReplies.add(courseMessageReply);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseMessage that = (CourseMessage) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
