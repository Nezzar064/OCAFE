package com.nezzar064.ocafe.model.entity;

import com.nezzar064.ocafe.model.Department;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@ToString
@Entity
@RequiredArgsConstructor
@Table(name = "course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_seq_gen")
    @SequenceGenerator(name = "course_seq_gen", sequenceName = "course_seq_gen", allocationSize = 1)
    private long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Department department;

    @ManyToMany
    @JoinTable(name = "course_participants",
            joinColumns = {@JoinColumn(name = "course_id") },
            inverseJoinColumns = {@JoinColumn(name = "person_id") })
    @ToString.Exclude
    private Set<Person> persons;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Subject> subjects;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<CourseMessage> courseMessages;

    public void setPersons(Set<Person> participants) {
        if (this.persons == null) {
            this.persons = new HashSet<>();
        }
        this.persons.clear();
        this.persons.addAll(participants);
    }

    public void removeCourseParticipant(Person person) {
        persons.remove(person);
        person.setCourses(null);
    }

    public void addCourseParticipant(Person person) {
        this.persons.add(person);
        person.setCourse(this);
    }

    /*
    public void setPersons(List<Person> persons) {
        if (this.persons == null) {
            this.persons = new ArrayList<>();
        }
        this.persons.addAll(persons);
    }
     */

    public void setSubjects(List<Subject> subjects) {
        if (this.subjects == null) {
            this.subjects = new ArrayList<>();
        }
        this.subjects.clear();
        this.subjects.addAll(subjects);
    }

    public void removeSubject(Subject subject) {
        subjects.remove(subject);
        subject.setCourse(null);
    }

    public void setMessages(List<CourseMessage> courseMessages) {
        if (this.courseMessages == null) {
            this.courseMessages = new ArrayList<>();
        }
        this.courseMessages.addAll(courseMessages);
    }

    public void addMessage(CourseMessage courseMessage) {
        if (this.courseMessages == null) {
            this.courseMessages = new ArrayList<>();
        }
        this.courseMessages.add(courseMessage);
        courseMessage.setCourse(this);
    }

    public void removeDashboardMessage(CourseMessage courseMessage) {
        courseMessages.remove(courseMessage);
        courseMessage.setCourse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

