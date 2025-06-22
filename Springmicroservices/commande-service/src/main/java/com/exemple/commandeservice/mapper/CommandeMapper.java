package com.exemple.commandeservice.mapper;

import com.exemple.commandeservice.domain.Commande;
import com.exemple.commandeservice.dto.AcheteurDTO;
import com.exemple.commandeservice.dto.CommandeDetailsResponseDTO;
import com.exemple.commandeservice.dto.ProduitDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommandeMapper {
    @Mapping(source = "commande.id", target = "id")
    @Mapping(source = "commande.dateCommande", target = "dateCommande")
    @Mapping(source = "commande.statut", target = "statut")
    @Mapping(source = "commande.quantite", target = "quantite")
    @Mapping(source = "acheteur", target = "acheteur")
    @Mapping(source = "produit", target = "produit")
    CommandeDetailsResponseDTO toDetailsDto(Commande commande, AcheteurDTO acheteur, ProduitDTO produit);
} 