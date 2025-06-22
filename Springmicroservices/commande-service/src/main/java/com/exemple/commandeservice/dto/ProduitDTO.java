package com.exemple.commandeservice.dto;

import lombok.Data;

@Data
public class ProduitDTO {
    private Long id;
    private String nom;
    private String description;
    private double prix;
} 