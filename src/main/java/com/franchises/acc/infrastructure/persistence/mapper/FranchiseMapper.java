package com.franchises.acc.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.franchises.acc.domain.model.Franchise;
import com.franchises.acc.infrastructure.persistence.entity.FranchiseEntity;

@Mapper(componentModel = "spring", uses = BranchMapper.class)
public interface FranchiseMapper {

    @Mapping(target = "branches", ignore = true)
    FranchiseEntity toEntity(Franchise domain);

    Franchise toDomain(FranchiseEntity entity);
}

