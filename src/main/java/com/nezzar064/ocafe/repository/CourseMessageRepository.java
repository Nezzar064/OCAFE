package com.nezzar064.ocafe.repository;

import com.nezzar064.ocafe.model.entity.CourseMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface CourseMessageRepository extends JpaRepository<CourseMessage, Long> {

    @Query("SELECT cm FROM CourseMessage cm WHERE cm.course.id = :courseId")
    Optional<List<CourseMessage>> getMessages(@Param("courseId") long courseId);

    @Query("SELECT cm FROM CourseMessage cm WHERE cm.course.id = :courseId")
    Optional<CourseMessage> getMessage(@Param("courseId") long courseId);

    Optional<CourseMessage> getCourseMessageByDate(Instant date);
}
