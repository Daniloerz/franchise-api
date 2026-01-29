package com.franchises.acc.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import com.franchises.acc.domain.service.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.franchises.acc.application.dto.request.CreateProductRequest;
import com.franchises.acc.application.dto.request.UpdateNameRequest;
import com.franchises.acc.application.dto.request.UpdateStockRequest;
import com.franchises.acc.application.dto.response.ProductResponse;
import com.franchises.acc.domain.exception.ResourceNotFoundException;
import com.franchises.acc.domain.model.Branch;
import com.franchises.acc.domain.model.Product;
import com.franchises.acc.domain.repository.BranchRepository;
import com.franchises.acc.domain.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Test
    void shouldAddProductToBranchSuccessfully() {
        // Given
        UUID branchId = UUID.randomUUID();
        when(branchRepository.findById(branchId))
                .thenReturn(Optional.of(Branch.builder().id(branchId).name("Branch").build()));

        Product savedProduct = Product.builder()
                .id(UUID.randomUUID())
                .name("Producto A")
                .stock(100)
                .branchId(branchId)
                .build();
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        CreateProductRequest request = new CreateProductRequest("Producto A", 100);

        // When
        ProductResponse response = productServiceImpl.addProductToBranch(branchId, request);

        // Then
        verify(productRepository).save(any(Product.class));
        assertThat(response.branchId()).isEqualTo(branchId);
        assertThat(response.stock()).isEqualTo(100);
    }

    @Test
    void shouldThrowExceptionWhenBranchNotFoundForProduct() {
        // Given
        UUID branchId = UUID.randomUUID();
        when(branchRepository.findById(branchId)).thenReturn(Optional.empty());

        CreateProductRequest request = new CreateProductRequest("Producto A", 100);

        // When / Then
        assertThatThrownBy(() -> productServiceImpl.addProductToBranch(branchId, request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        // Given
        UUID branchId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        when(productRepository.findByIdAndBranchId(productId, branchId))
                .thenReturn(Optional.of(Product.builder().id(productId).branchId(branchId).build()));

        // When
        productServiceImpl.deleteProduct(branchId, productId);

        // Then
        verify(productRepository).deleteById(productId);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundForDelete() {
        // Given
        UUID branchId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        when(productRepository.findByIdAndBranchId(productId, branchId))
                .thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> productServiceImpl.deleteProduct(branchId, productId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldUpdateProductStockSuccessfully() {
        // Given
        UUID branchId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Product product = Product.builder()
                .id(productId)
                .branchId(branchId)
                .stock(10)
                .build();
        when(productRepository.findByIdAndBranchId(productId, branchId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateStockRequest request = new UpdateStockRequest(50);

        // When
        ProductResponse response = productServiceImpl.updateProductStock(branchId, productId, request);

        // Then
        assertThat(response.stock()).isEqualTo(50);
        verify(productRepository).save(product);
    }

    @Test
    void shouldUpdateProductNameSuccessfully() {
        // Given
        UUID branchId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Product product = Product.builder()
                .id(productId)
                .branchId(branchId)
                .name("Old Name")
                .build();
        when(productRepository.findByIdAndBranchId(productId, branchId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateNameRequest request = new UpdateNameRequest("New Name");

        // When
        ProductResponse response = productServiceImpl.updateProductName(branchId, productId, request);

        // Then
        assertThat(response.name()).isEqualTo("New Name");
        verify(productRepository).save(product);
    }
}

