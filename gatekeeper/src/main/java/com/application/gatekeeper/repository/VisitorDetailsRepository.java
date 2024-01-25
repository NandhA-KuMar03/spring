package com.application.gatekeeper.repository;

import com.application.gatekeeper.entity.VisitorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface VisitorDetailsRepository extends JpaRepository<VisitorDetails, Integer> {

    List<VisitorDetails> findAllByDateOfVisit(Date date);

    List<VisitorDetails> findAllByVisitorVisitorId(int visitorId);

    List<VisitorDetails> findAllByVisitorVisitorIdAndDateOfVisitAfter(int visitorId, Date date);

    List<VisitorDetails> findAllByVisitorVisitorIdAndDateOfVisit(int visitorId, Date date);

    List<VisitorDetails> findAllByUserUserId(int userId);

    List<VisitorDetails> findAllByUserUserIdAndDateOfVisitAfter(int userId, Date date);

}
