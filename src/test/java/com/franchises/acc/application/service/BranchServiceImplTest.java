package com.franchises.acc.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.franchises.acc.domain.service.BranchServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.franchises.acc.application.dto.request.CreateBranchRequest;
import com.franchises.acc.application.dto.request.UpdateNameRequest;
import com.franchises.acc.application.dto.response.BranchResponse;
import com.franchises.acc.domain.exception.ResourceNotFoundException;
import com.franchises.acc.domain.model.Branch;
import com.franchises.acc.domain.model.Franchise;
import com.franchises.acc.domain.repository.BranchRepository;
import com.franchises.acc.domain.repository.FranchiseRepository;

@ExtendWith(MockitoExtension.class)
class BranchServiceImplTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BranchServiceImpl branchServiceImpl;

    @Test
    void shouldAddBranchToFranchiseSuccessfully() {
        // Given
        UUID franchiseId = UUID.randomUUID();
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Optional.of(Franchise.builder().id(franchiseId).name("Franchise").build()));

        Branch branchSaved = Branch.builder()
                .id(UUID.randomUUID())
                .name("Sucursal Centro")
                .franchiseId(franchiseId)
                .products(List.of())
                .build();
        when(branchRepository.save(any(Branch.class))).thenReturn(branchSaved);

        CreateBranchRequest request = new CreateBranchRequest("Sucursal Centro");

        // When
        BranchResponse response = branchServiceImpl.addBranchToFranchise(franchiseId, request);

        // Then
        verify(branchRepository).save(any(Branch.class));
        assertThat(response.franchiseId()).isEqualTo(franchiseId);
        assertThat(response.name()).isEqualTo("Sucursal Centro");
    }

    @Test
    void shouldThrowExceptionWhenFranchiseNotFoundForBranch() {
        // Given
        UUID franchiseId = UUID.randomUUID();
        when(franchiseRepository.findById(franchiseId)).thenReturn(Optional.empty());

        CreateBranchRequest request = new CreateBranchRequest("Sucursal Centro");

        // When / Then
        assertThatThrownBy(() -> branchServiceImpl.addBranchToFranchise(franchiseId, request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldUpdateBranchNameSuccessfully() {
        // Given
        UUID franchiseId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();

        Branch branch = Branch.builder()
                .id(branchId)
                .name("Old Name")
                .franchiseId(franchiseId)
                .build();
        when(branchRepository.findByIdAndFranchiseId(branchId, franchiseId))
                .thenReturn(Optional.of(branch));
        when(branchRepository.save(any(Branch.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateNameRequest request = new UpdateNameRequest("New Name");

        // When
        BranchResponse response = branchServiceImpl.updateBranchName(franchiseId, branchId, request);

        // Then
        assertThat(response.name()).isEqualTo("New Name");
        verify(branchRepository).save(branch);
    }

    @Test
    void shouldThrowExceptionWhenBranchNotFound() {
        // Given
        UUID franchiseId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();
        when(branchRepository.findByIdAndFranchiseId(branchId, franchiseId))
                .thenReturn(Optional.empty());

        UpdateNameRequest request = new UpdateNameRequest("New Name");

        // When / Then
        assertThatThrownBy(() -> branchServiceImpl.updateBranchName(franchiseId, branchId, request))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

