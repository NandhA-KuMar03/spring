package com.application.gatekeeper.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "visitor_details")
public class VisitorDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int visitorDetailId;
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "visitor_id")
    private Visitor visitor;
    private Date dateOfVisit;
    private String visitorPasskey;
    @Column(columnDefinition = "TINYINT(1)")
    private boolean isApproved;
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;
    private Time timeOfEntry;
    private Time timeOfExit;
    @Column(columnDefinition = "TINYINT(1)")
    private boolean hasVisited;

}
