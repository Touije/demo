package com.exemple.produitservice.events;

import lombok.Data;

@Data
public class StockADeduireEvent {
    private Long produitId;
    private int quantite;
} 