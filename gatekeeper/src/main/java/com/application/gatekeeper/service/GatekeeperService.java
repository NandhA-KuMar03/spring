package com.application.gatekeeper.service;

import com.application.gatekeeper.entity.Blacklist;
import com.application.gatekeeper.entity.VisitorDetails;
import com.application.gatekeeper.request.ApproveVisitorRequest;
import com.application.gatekeeper.request.BlacklistRequest;

import java.sql.Date;
import java.util.List;

public interface GatekeeperService {

    List<VisitorDetails> getVisitorsOnDate(Date date);

    VisitorDetails respondToVisitorScheduleRequest(ApproveVisitorRequest request);

    List<VisitorDetails> getAllVisitorDetailsOfParticularVisitor(int visitorId);

    List<VisitorDetails> getAllVisitorDetailsOfParticularVisitorActiveData(int visitorId);

    Blacklist blackListVisitor(BlacklistRequest request);

}
