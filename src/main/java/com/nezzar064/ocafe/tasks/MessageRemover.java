package com.nezzar064.ocafe.tasks;

import com.nezzar064.ocafe.repository.CourseRepository;
import com.nezzar064.ocafe.repository.DashboardMessageRepository;
import com.nezzar064.ocafe.service.impl.DashboardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MessageRemover {

    private DashboardServiceImpl dashboardService;

    @Autowired
    public MessageRemover(DashboardServiceImpl dashboardService) {
        this.dashboardService = dashboardService;
    }

    //Deletes all messages older than one month, every month on the first Friday of the Month, at noon
    @Scheduled(cron = "0 0 12 ? * 6#1")
    public void deleteDashboardMessagesAfterOneMonth() {
        dashboardService.deleteDashboardMessageAfterOneMonth();
    }
}
