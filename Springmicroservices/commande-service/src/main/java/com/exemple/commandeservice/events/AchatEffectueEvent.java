package com.exemple.commandeservice.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchatEffectueEvent {
    private Long acheteurId;
    private Long produitId;
    private int quantite;
} 