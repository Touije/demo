package com.exemple.produitservice.mapper;

import com.exemple.produitservice.domain.Produit;
import com.exemple.produitservice.dto.ProduitResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProduitMapper {

    ProduitResponseDTO toDto(Produit produit);
} 