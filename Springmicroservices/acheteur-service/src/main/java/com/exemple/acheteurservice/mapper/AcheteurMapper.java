package com.exemple.acheteurservice.mapper;

import com.exemple.acheteurservice.domain.Acheteur;
import com.exemple.acheteurservice.dto.AcheteurResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AcheteurMapper {
    AcheteurResponseDTO toDto(Acheteur acheteur);
} 