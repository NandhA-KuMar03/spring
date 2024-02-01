package com.application.gatekeeper.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditInfoRequest {

    private int userId;
    private String firstName;
    private String lastName;
    private Date dob;
    private String apartment;

}
