package com.nezzar064.ocafe.model.entity;

import com.nezzar064.ocafe.model.Department;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "dashboard")
public class Dashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dashboard_seq_gen")
    @SequenceGenerator(name = "dashboard_seq_gen", sequenceName = "dashboard_seq_gen", allocationSize = 1)
    private long id;

    @Enumerated(EnumType.STRING)
    private Department department;

    @OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<DashboardMessage> dashboardMessages;

    public void setMessages(List<DashboardMessage> dashboardMessages) {
        if (this.dashboardMessages == null) {
            this.dashboardMessages = new ArrayList<>();
        }
        this.dashboardMessages.addAll(dashboardMessages);
    }

    public void addMessage(DashboardMessage dashboardMessage) {
        if (this.dashboardMessages == null) {
            this.dashboardMessages = new ArrayList<>();
        }
        this.dashboardMessages.add(dashboardMessage);
        dashboardMessage.setDashboard(this);
    }

    public void removeDashboardMessage(DashboardMessage dashboardMessage) {
        dashboardMessages.remove(dashboardMessage);
        dashboardMessage.setDashboard(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dashboard dashboard = (Dashboard) o;
        return id == dashboard.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
