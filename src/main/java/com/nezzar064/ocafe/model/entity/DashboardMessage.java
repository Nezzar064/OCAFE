package com.nezzar064.ocafe.model.entity;

import com.nezzar064.ocafe.model.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "dashboard_messages")
@SuperBuilder
public class DashboardMessage extends Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dashboard_msg_seq_gen")
    @SequenceGenerator(name = "dashboard_msg_seq_gen", sequenceName = "dashboard_msg_seq_gen", allocationSize = 1)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dashboard_id")
    @ToString.Exclude
    private Dashboard dashboard;

    @ElementCollection
    @CollectionTable(name = "dashboard_message_replies", joinColumns = @JoinColumn(name = "dashboard_message_id"))
    private List<DashboardMessageReply> dashboardMessageReplies;

    public void addReply(DashboardMessageReply dashboardMessageReply) {
        if (this.dashboardMessageReplies == null) {
            dashboardMessageReplies = new ArrayList<>();
        }
        this.dashboardMessageReplies.add(dashboardMessageReply);
    }


}
