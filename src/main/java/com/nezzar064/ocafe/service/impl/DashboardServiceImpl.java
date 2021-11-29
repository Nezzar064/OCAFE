package com.nezzar064.ocafe.service.impl;

import com.nezzar064.ocafe.exception.DataNotFoundException;
import com.nezzar064.ocafe.model.Department;
import com.nezzar064.ocafe.model.dto.DashboardDto;
import com.nezzar064.ocafe.model.dto.DashboardMessageDto;
import com.nezzar064.ocafe.model.entity.CourseMessage;
import com.nezzar064.ocafe.model.entity.Dashboard;
import com.nezzar064.ocafe.model.entity.DashboardMessage;
import com.nezzar064.ocafe.model.entity.DashboardMessageReply;
import com.nezzar064.ocafe.payload.request.MessageReplyRequest;
import com.nezzar064.ocafe.payload.request.MessageRequest;
import com.nezzar064.ocafe.repository.DashboardMessageRepository;
import com.nezzar064.ocafe.repository.DashboardRepository;
import com.nezzar064.ocafe.service.interfaces.DashboardService;
import com.nezzar064.ocafe.service.mapper.DashboardMapper;
import com.nezzar064.ocafe.service.mapper.DashboardMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    private DashboardMapper dashboardMapper;
    private DashboardMessageMapper dashboardMessageMapper;
    private DashboardRepository dashboardRepository;
    private DashboardMessageRepository dashboardMessageRepository;

    @Autowired
    public DashboardServiceImpl(DashboardMapper dashboardMapper, DashboardMessageMapper dashboardMessageMapper, DashboardRepository dashboardRepository, DashboardMessageRepository dashboardMessageRepository) {
        this.dashboardMapper = dashboardMapper;
        this.dashboardMessageMapper = dashboardMessageMapper;
        this.dashboardRepository = dashboardRepository;
        this.dashboardMessageRepository = dashboardMessageRepository;
    }

    //Not used since no point in getting all dashboards
    @Override
    public List<DashboardDto> findAll() {
        return null;
    }

    public DashboardDto getDashboardByDepartment(String department) {
        switch (department) {
            case "computer-science":
                Dashboard computerScienceDashboard = dashboardRepository.getDashboardByDepartment(Department.COMPUTER_SCIENCE).orElseThrow(() -> new DataNotFoundException("Dashboard with department: " + department + " does not exist!"));
                return dashboardMapper.mapToDto(computerScienceDashboard);
            case "web-development":
                Dashboard webDevelopmentDashboard = dashboardRepository.getDashboardByDepartment(Department.WEB_DEVELOPMENT).orElseThrow(() -> new DataNotFoundException("Dashboard with department: " + department + " does not exist!"));
                return dashboardMapper.mapToDto(webDevelopmentDashboard);
            case "graphic-design":
                Dashboard graphicDesignDashboard = dashboardRepository.getDashboardByDepartment(Department.GRAPHIC_DESIGN).orElseThrow(() -> new DataNotFoundException("Dashboard with department: " + department + " does not exist!"));
                return dashboardMapper.mapToDto(graphicDesignDashboard);
            case "teachers":
                Dashboard teachersDashboard = dashboardRepository.getDashboardByDepartment(Department.TEACHERS).orElseThrow(() -> new DataNotFoundException("Dashboard with department: " + department + " does not exist!"));
                return dashboardMapper.mapToDto(teachersDashboard);
            case "students":
                Dashboard studentsDashboard = dashboardRepository.getDashboardByDepartment(Department.STUDENTS).orElseThrow(() -> new DataNotFoundException("Dashboard with department: " + department + " does not exist!"));
                return dashboardMapper.mapToDto(studentsDashboard);
            case "all":
                Dashboard allDashboard = dashboardRepository.getDashboardByDepartment(Department.ALL).orElseThrow(() -> new DataNotFoundException("Dashboard with department: " + department + " does not exist!"));
                return dashboardMapper.mapToDto(allDashboard);
        }
        Dashboard dashboard = dashboardRepository.getDashboardByDepartment(Department.ALL).orElseThrow(() -> new DataNotFoundException("Dashboard with department: " + department + " does not exist!"));
        return dashboardMapper.mapToDto(dashboard);
    }

    @Override
    public DashboardDto findById(Long id) {
        Dashboard dashboard = dashboardRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Dashboard with id: " + id + " does not exist!"));
        return dashboardMapper.mapToDto(dashboard);
    }

    @Override
    public DashboardDto save(DashboardDto dashboardDto) {
        Dashboard dashboard = new Dashboard();
        dashboard.setDepartment(dashboardDto.getDepartment());
        dashboardRepository.save(dashboard);
        return dashboardMapper.mapToDto(dashboard);
    }

    @Override
    public DashboardDto edit(DashboardDto dashboardDto, Long id) {
        Dashboard dashboard = dashboardRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Dashboard with id: " + id + " does not exist!"));
        dashboard.setId(dashboardDto.getId());
        dashboard.setDepartment(dashboardDto.getDepartment());
        dashboardRepository.save(dashboard);
        return dashboardMapper.mapToDto(dashboard);
    }

    @Override
    public void delete(Long id) {
        Dashboard dashboard = dashboardRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Dashboard with id: " + id + " does not exist!"));
        dashboardRepository.delete(dashboard);
    }

    public DashboardMessageDto addMessage(long dashboardId, MessageRequest dashboardMessageRequest, String authorName) {
        Dashboard dashboard = dashboardRepository.findById(dashboardId).orElseThrow(() -> new DataNotFoundException("Dashboard with id: " + dashboardId + " does not exist!"));
        DashboardMessage dashboardMessage = DashboardMessage.builder()
                .author(authorName)
                .subject(dashboardMessageRequest.getSubject())
                .content(dashboardMessageRequest.getContent())
                .date(Instant.now())
                .build();

        dashboard.addMessage(dashboardMessage);
        dashboardRepository.save(dashboard);
        DashboardMessage result = dashboardMessageRepository.getDashboardMessageByDate(dashboardMessage.getDate()).orElseThrow(() -> new DataNotFoundException("No dashboard message with date: " + dashboardMessage.getDate() + " exists!"));
        return dashboardMessageMapper.mapToDto(result);
    }

    public void deleteCourseMessageIfAuthorIsAuthor(long id, String authorName) {
        DashboardMessage dashboardMessage = dashboardMessageRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Dashboard message with id: " + id + " does not exist!"));
        if (dashboardMessage.getAuthor().equals(authorName)) {
            dashboardMessageRepository.delete(dashboardMessage);
        }
        else {
            throw new DataNotFoundException("Authors do not match - message could not be deleted!");
        }
    }

    public List<DashboardMessageDto> getDashboardMessagesForDashboard(long dashboardId) {
        return dashboardMessageMapper.mapEntityListToDtoList(dashboardMessageRepository.getMessages(dashboardId).orElseThrow(() -> new DataNotFoundException("No messages exist for dashboard: " + dashboardId)));
    }

    public DashboardMessageDto addReply(long dashboardMessageId, MessageReplyRequest dashboardMessageReplyRequest, String authorname) {
        DashboardMessage dashboardMessage = dashboardMessageRepository.findById(dashboardMessageId).orElseThrow(() -> new DataNotFoundException("Dashboard message with id: " + dashboardMessageId + " does not exist!"));
        DashboardMessageReply dashboardMessageReply = new DashboardMessageReply();
        dashboardMessageReply.setAuthor(authorname);
        dashboardMessageReply.setSubject("RE: " + dashboardMessage.getSubject());
        dashboardMessageReply.setContent(dashboardMessageReplyRequest.getContent());
        dashboardMessageReply.setDate(Instant.now());
        dashboardMessage.addReply(dashboardMessageReply);
        dashboardMessageRepository.save(dashboardMessage);
        return dashboardMessageMapper.mapToDto(dashboardMessage);
    }

    //Used for assisting the scheduled task in MessageRemover.class
    public void deleteDashboardMessageAfterOneMonth() {
        List<DashboardMessage> dashboardMessages = dashboardMessageRepository.findAll();
        List<DashboardMessage> dashboardMessagesToDelete = new ArrayList<>();
        Instant dateToday = Instant.now();
        for (int i = 0; i < dashboardMessages.size(); i++) {
            Instant indexDatePlusOneMonth = dashboardMessages.get(i).getDate().plus(30, ChronoUnit.DAYS);
            if (indexDatePlusOneMonth.isAfter(dateToday)) {
                dashboardMessagesToDelete.add(dashboardMessages.get(i));
            }
        }
        dashboardMessageRepository.deleteAll(dashboardMessagesToDelete);
    }
}
