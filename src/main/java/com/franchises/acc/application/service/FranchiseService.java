package com.franchises.acc.application.service;

import com.franchises.acc.application.dto.request.CreateFranchiseRequest;
import com.franchises.acc.application.dto.request.UpdateNameRequest;
import com.franchises.acc.application.dto.response.FranchiseResponse;
import com.franchises.acc.application.dto.response.TopStockProductResponse;

import java.util.List;
import java.util.UUID;

public interface FranchiseService {
    FranchiseResponse createFranchise(CreateFranchiseRequest request);
    FranchiseResponse updateFranchiseName(UUID franchiseId, UpdateNameRequest request);
    List<TopStockProductResponse> getTopStockProductsByFranchise(UUID franchiseId);

}
