package com.exemple.commandeservice.dto;

public class CreationCommandeRequestDTO {
    private Long acheteurId;
    private Long produitId;
    private int quantite;

    public Long getAcheteurId() { return acheteurId; }
    public void setAcheteurId(Long acheteurId) { this.acheteurId = acheteurId; }
    public Long getProduitId() { return produitId; }
    public void setProduitId(Long produitId) { this.produitId = produitId; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
} 