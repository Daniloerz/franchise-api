package com.franchises.acc.application.service;

import com.franchises.acc.application.dto.request.CreateProductRequest;
import com.franchises.acc.application.dto.request.UpdateNameRequest;
import com.franchises.acc.application.dto.request.UpdateStockRequest;
import com.franchises.acc.application.dto.response.ProductResponse;

import java.util.UUID;

public interface ProductService {
    public ProductResponse addProductToBranch(UUID branchId, CreateProductRequest request);
    void deleteProduct(UUID branchId, UUID productId);
    ProductResponse updateProductStock(UUID branchId, UUID productId, UpdateStockRequest request);
    ProductResponse updateProductName(UUID branchId, UUID productId, UpdateNameRequest request);
}
