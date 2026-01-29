package com.franchises.acc.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Franchise {

    private UUID id;
    private String name;

    @Builder.Default
    private List<Branch> branches = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
