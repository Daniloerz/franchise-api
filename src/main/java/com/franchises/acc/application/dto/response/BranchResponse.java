package com.franchises.acc.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record BranchResponse(
        UUID id,
        String name,
        UUID franchiseId,
        LocalDateTime createdAt
) {
}

