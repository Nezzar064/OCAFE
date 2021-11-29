package com.nezzar064.ocafe.model.entity;

import com.nezzar064.ocafe.model.PersonRole;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@RequiredArgsConstructor
@Table(name = "persons")
@SecondaryTable(name = "address", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id"))
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_seq_gen")
    @SequenceGenerator(name = "person_seq_gen", sequenceName = "person_seq_gen", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @Column(name = "first_name")
    @NotBlank(message = "Please provide a valid first name!")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Please provide a valid first name!")
    private String lastName;

    @Embedded
    @NotNull(message = "Please provide a valid address!")
    private Address address;

    @Column(name = "birth_date")
    @NotNull(message = "Please provide a valid date!")
    private LocalDate birthDate;

    @NotBlank(message = "Please provide a valid phone number!")
    @Length(max = 15, message = "Please provide a valid phone number! (max 15 char)")
    private String phone;

    @NotBlank(message = "Please provide a valid email!")
    private String email;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Please provide a valid role!")
    private PersonRole role;

    @ManyToMany(mappedBy = "persons")
    @ToString.Exclude
    private Set<Course> courses;

    @OneToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.DETACH},
    orphanRemoval = true)
    @JoinColumn(name = "user_id", unique = true)
    @ToString.Exclude
    private User user;

    public void setCourse(Course course) {
        this.courses.add(course);
    }

    public void removeCourses(Person person) {
        person.setCourses(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return 1422108840;
    }
}
