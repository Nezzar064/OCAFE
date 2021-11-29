package com.nezzar064.ocafe.repository;

import com.nezzar064.ocafe.model.Department;
import com.nezzar064.ocafe.model.entity.CourseMessage;
import com.nezzar064.ocafe.model.entity.Dashboard;
import com.nezzar064.ocafe.model.entity.DashboardMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, Long> {

    Optional<Dashboard> getDashboardByDepartment(Department department);
}
