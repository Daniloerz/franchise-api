package com.franchises.acc.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.franchises.acc.domain.model.Branch;
import com.franchises.acc.infrastructure.persistence.entity.BranchEntity;
import com.franchises.acc.infrastructure.persistence.entity.FranchiseEntity;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    @Mapping(target = "id", source = "domain.id")
    @Mapping(target = "name", source = "domain.name")
    @Mapping(target = "franchise", source = "franchise")
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", source = "domain.createdAt")
    @Mapping(target = "updatedAt", source = "domain.updatedAt")
    BranchEntity toEntity(Branch domain, FranchiseEntity franchise);

    @Mapping(target = "franchiseId", source = "entity.franchise.id")
    @Mapping(target = "products", ignore = true)
    Branch toDomain(BranchEntity entity);
}

