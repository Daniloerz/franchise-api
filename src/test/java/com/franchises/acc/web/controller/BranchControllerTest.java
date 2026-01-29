package com.franchises.acc.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import com.franchises.acc.application.service.BranchService;
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
import com.franchises.acc.application.dto.request.CreateBranchRequest;
import com.franchises.acc.application.dto.request.UpdateNameRequest;
import com.franchises.acc.application.dto.response.BranchResponse;
import com.franchises.acc.domain.exception.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BranchController.class)
@ActiveProfiles("test")
class BranchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BranchService branchServiceImpl;

    @Test
    void shouldAddBranch_Returns201() throws Exception {
        // Given
        UUID franchiseId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();
        BranchResponse response = new BranchResponse(branchId, "Sucursal Centro", franchiseId, null);
        when(branchServiceImpl.addBranchToFranchise(eq(franchiseId), any(CreateBranchRequest.class)))
                .thenReturn(response);

        CreateBranchRequest request = new CreateBranchRequest("Sucursal Centro");

        // When / Then
        mockMvc.perform(post("/api/v1/franchises/{franchiseId}/branches", franchiseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldAddBranch_Returns404_WhenFranchiseNotFound() throws Exception {
        // Given
        UUID franchiseId = UUID.randomUUID();
        CreateBranchRequest request = new CreateBranchRequest("Sucursal Centro");

        when(branchServiceImpl.addBranchToFranchise(eq(franchiseId), any(CreateBranchRequest.class)))
                .thenThrow(new ResourceNotFoundException("Franchise", "id", franchiseId));

        // When / Then
        mockMvc.perform(post("/api/v1/franchises/{franchiseId}/branches", franchiseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateBranchName_Returns200() throws Exception {
        // Given
        UUID franchiseId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();
        BranchResponse response = new BranchResponse(branchId, "New Name", franchiseId, null);
        when(branchServiceImpl.updateBranchName(eq(franchiseId), eq(branchId), any(UpdateNameRequest.class)))
                .thenReturn(response);

        UpdateNameRequest request = new UpdateNameRequest("New Name");

        // When / Then
        mockMvc.perform(patch("/api/v1/franchises/{franchiseId}/branches/{branchId}/name", franchiseId, branchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}

