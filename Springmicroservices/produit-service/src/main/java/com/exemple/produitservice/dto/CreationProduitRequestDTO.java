package com.exemple.produitservice.dto;

import lombok.Data;

@Data
public class CreationProduitRequestDTO {
    private String nom;
    private String description;
    private double prix;
    private int quantiteEnStock;
} 