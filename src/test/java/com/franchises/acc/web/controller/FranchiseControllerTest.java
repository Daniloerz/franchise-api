package com.franchises.acc.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import com.franchises.acc.application.service.FranchiseService;
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
import com.franchises.acc.application.dto.request.CreateFranchiseRequest;
import com.franchises.acc.application.dto.request.UpdateNameRequest;
import com.franchises.acc.application.dto.response.FranchiseResponse;
import com.franchises.acc.application.dto.response.TopStockProductResponse;
import com.franchises.acc.domain.exception.DuplicateResourceException;
import com.franchises.acc.domain.exception.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FranchiseController.class)
@ActiveProfiles("test")
class FranchiseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FranchiseService franchiseServiceImpl;

    @Test
    void shouldCreateFranchise_Returns201() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        FranchiseResponse response = new FranchiseResponse(id, "Test Franchise", null);
        when(franchiseServiceImpl.createFranchise(any(CreateFranchiseRequest.class))).thenReturn(response);

        CreateFranchiseRequest request = new CreateFranchiseRequest("Test Franchise");

        // When / Then
        mockMvc.perform(post("/api/v1/franchises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Test Franchise"));
    }

    @Test
    void shouldCreateFranchise_Returns400_WhenNameIsBlank() throws Exception {
        // Given
        CreateFranchiseRequest request = new CreateFranchiseRequest(" ");

        // When / Then
        mockMvc.perform(post("/api/v1/franchises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateFranchise_Returns409_WhenNameExists() throws Exception {
        // Given
        CreateFranchiseRequest request = new CreateFranchiseRequest("Existing");
        when(franchiseServiceImpl.createFranchise(any(CreateFranchiseRequest.class)))
                .thenThrow(new DuplicateResourceException("Franchise", "name", "Existing"));

        // When / Then
        mockMvc.perform(post("/api/v1/franchises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldUpdateFranchiseName_Returns200() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        FranchiseResponse response = new FranchiseResponse(id, "New Name", null);
        when(franchiseServiceImpl.updateFranchiseName(eq(id), any(UpdateNameRequest.class))).thenReturn(response);

        UpdateNameRequest request = new UpdateNameRequest("New Name");

        // When / Then
        mockMvc.perform(patch("/api/v1/franchises/{id}/name", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"));
    }

    @Test
    void shouldUpdateFranchiseName_Returns404_WhenNotFound() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        UpdateNameRequest request = new UpdateNameRequest("New Name");
        when(franchiseServiceImpl.updateFranchiseName(eq(id), any(UpdateNameRequest.class)))
                .thenThrow(new ResourceNotFoundException("Franchise", "id", id));

        // When / Then
        mockMvc.perform(patch("/api/v1/franchises/{id}/name", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetTopStockProducts_Returns200() throws Exception {
        // Given
        UUID franchiseId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();
        TopStockProductResponse dto = new TopStockProductResponse(
                UUID.randomUUID(),
                "Product A",
                100,
                branchId,
                "Branch A"
        );
        when(franchiseServiceImpl.getTopStockProductsByFranchise(franchiseId))
                .thenReturn(List.of(dto));

        // When / Then
        mockMvc.perform(get("/api/v1/franchises/{id}/top-stock-products", franchiseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Product A"))
                .andExpect(jsonPath("$[0].stock").value(100))
                .andExpect(jsonPath("$[0].branchId").value(branchId.toString()))
                .andExpect(jsonPath("$[0].branchName").value("Branch A"));
    }
}

