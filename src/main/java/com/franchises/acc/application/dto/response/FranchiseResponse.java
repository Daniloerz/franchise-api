package com.franchises.acc.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record FranchiseResponse(
        UUID id,
        String name,
        LocalDateTime createdAt
) {
}

