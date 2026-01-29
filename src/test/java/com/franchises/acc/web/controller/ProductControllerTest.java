package com.franchises.acc.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import com.franchises.acc.application.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franchises.acc.application.dto.request.CreateProductRequest;
import com.franchises.acc.application.dto.request.UpdateNameRequest;
import com.franchises.acc.application.dto.request.UpdateStockRequest;
import com.franchises.acc.application.dto.response.ProductResponse;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productServiceImpl;

    @Test
    void shouldAddProduct_Returns201() throws Exception {
        // Given
        UUID branchId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        ProductResponse response = new ProductResponse(productId, "Producto A", 100, branchId, null);
        when(productServiceImpl.addProductToBranch(eq(branchId), any(CreateProductRequest.class)))
                .thenReturn(response);

        CreateProductRequest request = new CreateProductRequest("Producto A", 100);

        // When / Then
        mockMvc.perform(post("/api/v1/branches/{branchId}/products", branchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldAddProduct_Returns400_WhenStockIsNegative() throws Exception {
        // Given
        UUID branchId = UUID.randomUUID();
        CreateProductRequest request = new CreateProductRequest("Producto A", -1);

        // When / Then
        mockMvc.perform(post("/api/v1/branches/{branchId}/products", branchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteProduct_Returns204() throws Exception {
        // Given
        UUID branchId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        doNothing().when(productServiceImpl).deleteProduct(branchId, productId);

        // When / Then
        mockMvc.perform(delete("/api/v1/branches/{branchId}/products/{productId}", branchId, productId))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldUpdateProductStock_Returns200() throws Exception {
        // Given
        UUID branchId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        ProductResponse response = new ProductResponse(productId, "Producto A", 50, branchId, null);
        when(productServiceImpl.updateProductStock(eq(branchId), eq(productId), any(UpdateStockRequest.class)))
                .thenReturn(response);

        UpdateStockRequest request = new UpdateStockRequest(50);

        // When / Then
        mockMvc.perform(patch("/api/v1/branches/{branchId}/products/{productId}/stock", branchId, productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateProductName_Returns200() throws Exception {
        // Given
        UUID branchId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        ProductResponse response = new ProductResponse(productId, "New Name", 50, branchId, null);
        when(productServiceImpl.updateProductName(eq(branchId), eq(productId), any(UpdateNameRequest.class)))
                .thenReturn(response);

        UpdateNameRequest request = new UpdateNameRequest("New Name");

        // When / Then
        mockMvc.perform(patch("/api/v1/branches/{branchId}/products/{productId}/name", branchId, productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}

