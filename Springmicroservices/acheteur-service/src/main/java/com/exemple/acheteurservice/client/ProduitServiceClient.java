package com.exemple.acheteurservice.client;

import com.exemple.acheteurservice.dto.ProduitDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "produit-service")
public interface ProduitServiceClient {

    @GetMapping("/produits/{id}")
    ProduitDTO getProduitById(@PathVariable("id") Long id);
} 