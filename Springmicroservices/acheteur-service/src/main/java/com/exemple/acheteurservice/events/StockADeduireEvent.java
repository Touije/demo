package com.exemple.acheteurservice.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockADeduireEvent {
    private Long produitId;
    private int quantite;
} 