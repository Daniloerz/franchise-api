package com.franchises.acc.web.controller;

import java.net.URI;
import java.util.UUID;

import com.franchises.acc.application.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.franchises.acc.application.dto.request.CreateProductRequest;
import com.franchises.acc.application.dto.request.UpdateNameRequest;
import com.franchises.acc.application.dto.request.UpdateStockRequest;
import com.franchises.acc.application.dto.response.ProductResponse;
import com.franchises.acc.domain.service.ProductServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/branches/{branchId}/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Gestión de productos de sucursales")
public class ProductController {

    private final ProductService productServiceImpl;

    @PostMapping
    @Operation(
            summary = "Agregar un producto a una sucursal",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Producto creado correctamente"),
                    @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
            }
    )
    public ResponseEntity<ProductResponse> addProductToBranch(
            @PathVariable UUID branchId,
            @Valid @RequestBody CreateProductRequest request
    ) {
        ProductResponse response = productServiceImpl.addProductToBranch(branchId, request);
        URI location = URI.create("/api/v1/branches/" + branchId + "/products/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/{productId}")
    @Operation(
            summary = "Eliminar un producto de una sucursal",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
                    @ApiResponse(responseCode = "404", description = "Producto o sucursal no encontrada")
            }
    )
    public ResponseEntity<Void> deleteProduct(
            @PathVariable UUID branchId,
            @PathVariable UUID productId
    ) {
        productServiceImpl.deleteProduct(branchId, productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{productId}/stock")
    @Operation(
            summary = "Actualizar el stock de un producto",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Stock actualizado correctamente"),
                    @ApiResponse(responseCode = "404", description = "Producto o sucursal no encontrada")
            }
    )
    public ResponseEntity<ProductResponse> updateProductStock(
            @PathVariable UUID branchId,
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateStockRequest request
    ) {
        ProductResponse response = productServiceImpl.updateProductStock(branchId, productId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{productId}/name")
    @Operation(
            summary = "Actualizar el nombre de un producto",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Nombre actualizado correctamente"),
                    @ApiResponse(responseCode = "404", description = "Producto o sucursal no encontrada")
            }
    )
    public ResponseEntity<ProductResponse> updateProductName(
            @PathVariable UUID branchId,
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateNameRequest request
    ) {
        ProductResponse response = productServiceImpl.updateProductName(branchId, productId, request);
        return ResponseEntity.ok(response);
    }
}

