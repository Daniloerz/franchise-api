package com.franchises.acc.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateBranchRequest(
        @NotBlank String name
) {
}

