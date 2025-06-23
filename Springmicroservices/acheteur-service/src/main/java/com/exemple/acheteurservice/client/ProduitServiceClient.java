package com.exemple.acheteurservice.client;

import com.exemple.acheteurservice.dto.ProduitDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "produit-service")
public interface ProduitServiceClient {

    @GetMapping("/produits/{id}")
    ProduitDTO getProduitById(@PathVariable("id") Long id);

    @PostMapping("/produits/{id}/decrementer-stock")
    void decrementerStock(@PathVariable("id") Long id, @RequestParam("quantite") int quantite);
} 