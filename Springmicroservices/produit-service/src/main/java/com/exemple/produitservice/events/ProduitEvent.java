package com.exemple.produitservice.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitEvent {
    private Long id;
    private String nom;
    private String description;
    private double prix;
    private int quantiteEnStock;
} 