package com.exemple.commandeservice.dto;

import lombok.Data;

@Data
public class AcheteurDTO {
    private Long id;
    private String nom;
    private String email;
    private String adresse;
    private String telephone;
} 