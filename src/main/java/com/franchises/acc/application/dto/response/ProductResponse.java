package com.franchises.acc.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        Integer stock,
        UUID branchId,
        LocalDateTime createdAt
) {
}

