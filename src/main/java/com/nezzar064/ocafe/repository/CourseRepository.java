package com.nezzar064.ocafe.repository;

import com.nezzar064.ocafe.model.dto.CourseListDto;
import com.nezzar064.ocafe.model.dto.CourseMessageDto;
import com.nezzar064.ocafe.model.entity.Course;
import com.nezzar064.ocafe.model.entity.CourseMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT new com.nezzar064.ocafe.model.dto.CourseListDto(c.id, c.name, c.department) FROM Course c ORDER BY c.department")
    Optional<List<CourseListDto>> findAllCoursesWithIdNameAndDepartment();

}