package com.franchises.acc.web.controller;

import java.net.URI;
import java.util.UUID;

import com.franchises.acc.application.service.BranchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.franchises.acc.application.dto.request.CreateBranchRequest;
import com.franchises.acc.application.dto.request.UpdateNameRequest;
import com.franchises.acc.application.dto.response.BranchResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/franchises/{franchiseId}/branches")
@RequiredArgsConstructor
@Tag(name = "Branches", description = "Gestión de sucursales de franquicias")
public class BranchController {

    private final BranchService branchServiceImpl;

    @PostMapping
    @Operation(
            summary = "Agregar una sucursal a una franquicia",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Sucursal creada correctamente"),
                    @ApiResponse(responseCode = "404", description = "Franquicia no encontrada")
            }
    )
    public ResponseEntity<BranchResponse> addBranchToFranchise(
            @PathVariable UUID franchiseId,
            @Valid @RequestBody CreateBranchRequest request
    ) {
        BranchResponse response = branchServiceImpl.addBranchToFranchise(franchiseId, request);
        URI location = URI.create("/api/v1/franchises/" + franchiseId + "/branches/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @PatchMapping("/{branchId}/name")
    @Operation(
            summary = "Actualizar el nombre de una sucursal",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sucursal actualizada correctamente"),
                    @ApiResponse(responseCode = "404", description = "Sucursal o franquicia no encontrada")
            }
    )
    public ResponseEntity<BranchResponse> updateBranchName(
            @PathVariable UUID franchiseId,
            @PathVariable UUID branchId,
            @Valid @RequestBody UpdateNameRequest request
    ) {
        BranchResponse response = branchServiceImpl.updateBranchName(franchiseId, branchId, request);
        return ResponseEntity.ok(response);
    }
}

