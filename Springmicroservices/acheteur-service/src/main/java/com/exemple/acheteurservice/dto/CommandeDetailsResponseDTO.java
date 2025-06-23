package com.exemple.acheteurservice.dto;

import java.time.LocalDateTime;

public class CommandeDetailsResponseDTO {
    private Long id;
    private LocalDateTime dateCommande;
    private String statut;
    private int quantite;
    private AcheteurDTO acheteur;
    private ProduitDTO produit;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDateCommande() { return dateCommande; }
    public void setDateCommande(LocalDateTime dateCommande) { this.dateCommande = dateCommande; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public AcheteurDTO getAcheteur() { return acheteur; }
    public void setAcheteur(AcheteurDTO acheteur) { this.acheteur = acheteur; }
    public ProduitDTO getProduit() { return produit; }
    public void setProduit(ProduitDTO produit) { this.produit = produit; }
} 