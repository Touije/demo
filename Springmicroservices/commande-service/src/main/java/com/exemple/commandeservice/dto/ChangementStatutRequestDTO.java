package com.exemple.commandeservice.dto;

import com.exemple.commandeservice.domain.StatutCommande;
import lombok.Data;

@Data
public class ChangementStatutRequestDTO {
    private StatutCommande nouveauStatut;
} 