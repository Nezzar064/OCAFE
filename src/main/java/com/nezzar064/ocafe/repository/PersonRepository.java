package com.nezzar064.ocafe.repository;

import com.nezzar064.ocafe.model.PersonRole;
import com.nezzar064.ocafe.model.dto.CourseParticipantsDto;
import com.nezzar064.ocafe.model.dto.PersonDto;
import com.nezzar064.ocafe.model.dto.PersonListDto;
import com.nezzar064.ocafe.model.entity.Course;
import com.nezzar064.ocafe.model.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT p FROM Person p WHERE p.id = :id AND p.role = :role")
    Optional<Person> getByIdAndRole(@Param("id") long id, @Param("role") PersonRole role);

    @Query("SELECT p FROM Person p JOIN FETCH p.courses WHERE p.id = :id")
    Optional<Person> getPersonByIdWithCourses(@Param("id") long personId);

    @Query("SELECT new com.nezzar064.ocafe.model.dto.PersonListDto(p.id, p.firstName, p.lastName) FROM Person p WHERE p.role = :role")
    Optional<List<PersonListDto>> getPersonsIdNameAndLastNameFromRole(@Param("role") PersonRole role);

    @Query("SELECT new com.nezzar064.ocafe.model.dto.PersonDto(p.firstName, p.lastName) FROM Person p WHERE p.user.username = :username")
    Optional<PersonDto> getNameFromUsername(@Param("username") String username);
}
