package com.application.gatekeeper.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitorRequest {

    private String email;
    private Date dateOfVisit;
    private Time timeOfEntry;
    private Time timeOfExit;

}
