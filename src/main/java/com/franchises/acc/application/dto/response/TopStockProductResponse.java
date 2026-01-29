package com.franchises.acc.application.dto.response;

import java.util.UUID;

public record TopStockProductResponse(
        UUID productId,
        String productName,
        Integer stock,
        UUID branchId,
        String branchName
) {
}

