package com.exemple.commandeservice.service;

import com.exemple.commandeservice.domain.StatutCommande;
import com.exemple.commandeservice.dto.CommandeDetailsResponseDTO;

import java.util.List;

public interface CommandeServiceInterface {
    List<CommandeDetailsResponseDTO> getAllCommandesWithDetails();
    CommandeDetailsResponseDTO getCommandeById(Long id);
    void deleteCommande(Long id);
    void changerStatut(Long commandeId, StatutCommande nouveauStatut);
} 