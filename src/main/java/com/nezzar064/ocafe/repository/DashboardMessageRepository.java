package com.nezzar064.ocafe.repository;

import com.nezzar064.ocafe.model.entity.CourseMessage;
import com.nezzar064.ocafe.model.entity.DashboardMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface DashboardMessageRepository extends JpaRepository<DashboardMessage, Long> {

    @Query("SELECT dm FROM DashboardMessage dm WHERE dm.dashboard.id = :dashboardId")
    Optional<List<DashboardMessage>> getMessages(@Param("dashboardId") long dashboardId);

    @Query("SELECT dm FROM DashboardMessage dm WHERE dm.dashboard.id = :dashboardId")
    Optional<DashboardMessage> getMessage(@Param("dashboardId") long dashboardId);

    Optional<DashboardMessage> getDashboardMessageByDate(Instant date);
}
