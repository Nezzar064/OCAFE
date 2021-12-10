package com.nezzar064.ocafe.controller;

import com.nezzar064.ocafe.model.dto.*;
import com.nezzar064.ocafe.payload.request.MessageReplyRequest;
import com.nezzar064.ocafe.payload.request.MessageRequest;
import com.nezzar064.ocafe.service.impl.DashboardServiceImpl;
import com.nezzar064.ocafe.service.impl.PersonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/api/dashboard-management")
public class DashboardController {

    private final Logger log = LoggerFactory.getLogger(DashboardController.class);

    private DashboardServiceImpl dashboardService;
    private PersonServiceImpl personService;

    @Autowired
    public DashboardController(DashboardServiceImpl dashboardService, PersonServiceImpl personService) {
        this.dashboardService = dashboardService;
        this.personService = personService;
    }

    @GetMapping("/dashboards")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DashboardDto> getAllDashboardsForDepartment(@RequestParam(value="department") String department) {
        DashboardDto result = dashboardService.getDashboardByDepartment(department);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/dashboards/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DashboardDto> getDashboardById(@PathVariable long id) {
        DashboardDto result = dashboardService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/dashboards/add")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DashboardDto> createDashboard(@Valid @RequestBody DashboardDto dashboardDto) throws URISyntaxException {
        log.info("Request to create Dashboard: {}", dashboardDto);
        DashboardDto result = dashboardService.save(dashboardDto);
        return ResponseEntity.created(new URI("/api/dashboard-management/students/" + result.getId())).body((result));

    }

    @PutMapping("/dashboards/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DashboardDto> editDashboard(@PathVariable long id, @Valid @RequestBody DashboardDto dashboardDto) {
        log.info("Request to update Dashboard: {}", dashboardDto);
        DashboardDto result = dashboardService.edit(dashboardDto, id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/dashboards/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteDashboard(@PathVariable("id") long id) {
        log.info("Request to delete Dashboard: {}", id);
        dashboardService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dashboards/{id}/messages")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<DashboardMessageDto>> getMessagesForDashboard(@PathVariable("id") long dashboardId) {
        List<DashboardMessageDto> dashboardMessages = dashboardService.getDashboardMessagesForDashboard(dashboardId);
        return new ResponseEntity<>(dashboardMessages, HttpStatus.OK);
    }

    @PostMapping("/dashboards/{id}/messages/add")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DashboardMessageDto> addMessageToDashboard(@PathVariable("id") long dashboardId, @Valid @RequestBody MessageRequest dashboardMessageRequest) {
        log.info("Request to add message to Dashboard: {}", dashboardMessageRequest);
        PersonDto personDto = personService.getPersonNameFromUsername(getCurrentAuthenticatedUsersUsername());
        String authorName = personDto.getFirstName() + " " + personDto.getLastName();
        DashboardMessageDto result = dashboardService.addMessage(dashboardId, dashboardMessageRequest, authorName);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/dashboards/messages/{id}/reply")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DashboardMessageDto> replyToMessage(@PathVariable("id") long messageId, @Valid @RequestBody MessageReplyRequest dashboardMessageReplyRequest) {
        log.info("Request to add reply to Message: {}", dashboardMessageReplyRequest);
        PersonDto personDto = personService.getPersonNameFromUsername(getCurrentAuthenticatedUsersUsername());
        String authorName = personDto.getFirstName() + " " + personDto.getLastName();
        DashboardMessageDto result = dashboardService.addReply(messageId, dashboardMessageReplyRequest, authorName);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    private String getCurrentAuthenticatedUsersUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
