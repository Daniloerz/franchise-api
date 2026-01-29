package com.franchises.acc.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.franchises.acc.domain.service.FranchiseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.franchises.acc.application.dto.request.CreateFranchiseRequest;
import com.franchises.acc.application.dto.request.UpdateNameRequest;
import com.franchises.acc.application.dto.response.FranchiseResponse;
import com.franchises.acc.application.dto.response.TopStockProductResponse;
import com.franchises.acc.domain.exception.DuplicateResourceException;
import com.franchises.acc.domain.exception.ResourceNotFoundException;
import com.franchises.acc.domain.model.Branch;
import com.franchises.acc.domain.model.Franchise;
import com.franchises.acc.domain.model.Product;
import com.franchises.acc.domain.repository.BranchRepository;
import com.franchises.acc.domain.repository.FranchiseRepository;
import com.franchises.acc.domain.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class FranchiseServiceImplTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private FranchiseServiceImpl franchiseServiceImpl;

    @Test
    void shouldCreateFranchiseSuccessfully() {
        // Given
        CreateFranchiseRequest request = new CreateFranchiseRequest("Test Franchise");
        when(franchiseRepository.existsByName("Test Franchise")).thenReturn(false);

        UUID id = UUID.randomUUID();
        Franchise savedFranchise = Franchise.builder()
                .id(id)
                .name("Test Franchise")
                .build();
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(savedFranchise);

        // When
        FranchiseResponse response = franchiseServiceImpl.createFranchise(request);

        // Then
        verify(franchiseRepository).save(any(Franchise.class));
        assertThat(response.name()).isEqualTo("Test Franchise");
        assertThat(response.id()).isEqualTo(id);
    }

    @Test
    void shouldThrowExceptionWhenFranchiseNameExists() {
        // Given
        CreateFranchiseRequest request = new CreateFranchiseRequest("Existing Franchise");
        when(franchiseRepository.existsByName("Existing Franchise")).thenReturn(true);

        // When / Then
        assertThatThrownBy(() -> franchiseServiceImpl.createFranchise(request))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void shouldUpdateFranchiseNameSuccessfully() {
        // Given
        UUID franchiseId = UUID.randomUUID();
        Franchise existing = Franchise.builder()
                .id(franchiseId)
                .name("Old Name")
                .build();
        when(franchiseRepository.findById(franchiseId)).thenReturn(Optional.of(existing));
        when(franchiseRepository.save(any(Franchise.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateNameRequest request = new UpdateNameRequest("New Name");

        // When
        FranchiseResponse response = franchiseServiceImpl.updateFranchiseName(franchiseId, request);

        // Then
        assertThat(response.name()).isEqualTo("New Name");
        verify(franchiseRepository).save(existing);
    }

    @Test
    void shouldThrowExceptionWhenFranchiseNotFound() {
        // Given
        UUID franchiseId = UUID.randomUUID();
        when(franchiseRepository.findById(franchiseId)).thenReturn(Optional.empty());

        UpdateNameRequest request = new UpdateNameRequest("New Name");

        // When / Then
        assertThatThrownBy(() -> franchiseServiceImpl.updateFranchiseName(franchiseId, request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldGetTopStockProductsByFranchise() {
        // Given
        UUID franchiseId = UUID.randomUUID();
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Optional.of(Franchise.builder().id(franchiseId).name("Franchise").build()));

        UUID branchId = UUID.randomUUID();
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name("Product A")
                .stock(100)
                .branchId(branchId)
                .build();
        when(productRepository.findTopStockProductsByFranchiseId(franchiseId))
                .thenReturn(List.of(product));

        Branch branch = Branch.builder()
                .id(branchId)
                .name("Branch A")
                .build();
        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));

        // When
        List<TopStockProductResponse> result = franchiseServiceImpl.getTopStockProductsByFranchise(franchiseId);

        // Then
        assertThat(result).hasSize(1);
        TopStockProductResponse response = result.get(0);
        assertThat(response.productName()).isEqualTo("Product A");
        assertThat(response.stock()).isEqualTo(100);
        assertThat(response.branchId()).isEqualTo(branchId);
        assertThat(response.branchName()).isEqualTo("Branch A");
    }
}

