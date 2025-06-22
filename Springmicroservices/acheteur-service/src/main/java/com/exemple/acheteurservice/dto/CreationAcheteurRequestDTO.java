package com.exemple.acheteurservice.dto;

import lombok.Data;

@Data
public class CreationAcheteurRequestDTO {
    private String nom;
    private String email;
    private String adresse;
    private String telephone;
} 