package com.franchises.acc.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.franchises.acc.domain.model.Product;
import com.franchises.acc.infrastructure.persistence.entity.BranchEntity;
import com.franchises.acc.infrastructure.persistence.entity.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", source = "domain.id")
    @Mapping(target = "name", source = "domain.name")
    @Mapping(target = "stock", source = "domain.stock")
    @Mapping(target = "branch", source = "branch")
    @Mapping(target = "createdAt", source = "domain.createdAt")
    @Mapping(target = "updatedAt", source = "domain.updatedAt")
    ProductEntity toEntity(Product domain, BranchEntity branch);

    @Mapping(target = "branchId", source = "entity.branch.id")
    Product toDomain(ProductEntity entity);
}

