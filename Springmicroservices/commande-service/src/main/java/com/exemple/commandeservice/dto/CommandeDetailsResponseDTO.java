package com.exemple.commandeservice.dto;

import com.exemple.commandeservice.domain.StatutCommande;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommandeDetailsResponseDTO {
    private Long id;
    private LocalDateTime dateCommande;
    private StatutCommande statut;
    private int quantite;
    private AcheteurDTO acheteur;
    private ProduitDTO produit;
}
