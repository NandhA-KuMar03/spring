package com.application.gatekeeper.service;

import com.application.gatekeeper.entity.User;
import com.application.gatekeeper.entity.UserDetails;
import com.application.gatekeeper.request.EditInfoRequest;

import java.util.List;

public interface AdminService {

    List<User> getAwaitingApprovals();

    UserDetails getUserAwaitingApproval(int userId);

    User approveUser(int userId);

    UserDetails editInfo(EditInfoRequest request);

    void removeUser(int userId);

}
