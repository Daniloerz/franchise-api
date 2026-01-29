package com.franchises.acc.application.service;

import com.franchises.acc.application.dto.request.CreateBranchRequest;
import com.franchises.acc.application.dto.request.UpdateNameRequest;
import com.franchises.acc.application.dto.response.BranchResponse;

import java.util.UUID;

public interface BranchService {
    BranchResponse addBranchToFranchise(UUID franchiseId, CreateBranchRequest request);
    BranchResponse updateBranchName(UUID franchiseId, UUID branchId, UpdateNameRequest request);
}
