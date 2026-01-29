package com.franchises.acc.web.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import com.franchises.acc.application.service.FranchiseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.franchises.acc.application.dto.request.CreateFranchiseRequest;
import com.franchises.acc.application.dto.request.UpdateNameRequest;
import com.franchises.acc.application.dto.response.FranchiseResponse;
import com.franchises.acc.application.dto.response.TopStockProductResponse;
import com.franchises.acc.domain.service.FranchiseServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/franchises")
@RequiredArgsConstructor
@Tag(name = "Franchises", description = "Gestión de franquicias")
public class FranchiseController {

    private final FranchiseService franchiseServiceImpl;

    @PostMapping
    @Operation(
            summary = "Crear una nueva franquicia",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Franquicia creada correctamente")
            }
    )
    public ResponseEntity<FranchiseResponse> createFranchise(@Valid @RequestBody CreateFranchiseRequest request) {
        FranchiseResponse response = franchiseServiceImpl.createFranchise(request);
        URI location = URI.create("/api/v1/franchises/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @PatchMapping("/{franchiseId}/name")
    @Operation(
            summary = "Actualizar el nombre de una franquicia",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Franquicia actualizada correctamente"),
                    @ApiResponse(responseCode = "404", description = "Franquicia no encontrada")
            }
    )
    public ResponseEntity<FranchiseResponse> updateFranchiseName(
            @PathVariable UUID franchiseId,
            @Valid @RequestBody UpdateNameRequest request
    ) {
        FranchiseResponse response = franchiseServiceImpl.updateFranchiseName(franchiseId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{franchiseId}/top-stock-products")
    @Operation(
            summary = "Obtener los productos con más stock por sucursal de una franquicia",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado devuelto correctamente"),
                    @ApiResponse(responseCode = "404", description = "Franquicia no encontrada")
            }
    )
    public ResponseEntity<List<TopStockProductResponse>> getTopStockProductsByFranchise(
            @PathVariable UUID franchiseId
    ) {
        List<TopStockProductResponse> response = franchiseServiceImpl.getTopStockProductsByFranchise(franchiseId);
        return ResponseEntity.ok(response);
    }
}

