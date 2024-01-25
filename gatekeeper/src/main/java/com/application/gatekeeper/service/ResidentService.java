package com.application.gatekeeper.service;

import com.application.gatekeeper.entity.Blacklist;
import com.application.gatekeeper.entity.Visitor;
import com.application.gatekeeper.entity.VisitorDetails;
import com.application.gatekeeper.request.BlacklistRequest;
import com.application.gatekeeper.request.VisitorRegisterRequest;
import com.application.gatekeeper.request.VisitorRequest;

import java.util.List;

public interface ResidentService {

    Visitor createAndScheduleVisitor(VisitorRegisterRequest request);
    VisitorDetails scheduleVisitor(VisitorRequest request);
    Blacklist blackListVisitor(BlacklistRequest request);
    List<VisitorDetails> getVisitors();
    List<VisitorDetails> getActiveVisitors();

}
