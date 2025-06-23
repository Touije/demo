package com.exemple.acheteurservice.dto;

public class ProduitDTO {
    private Long id;
    private String nom;
    private String description;
    private double prix;
    private int quantiteEnStock;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    public int getQuantiteEnStock() { return quantiteEnStock; }
    public void setQuantiteEnStock(int quantiteEnStock) { this.quantiteEnStock = quantiteEnStock; }
} 