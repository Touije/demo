package com.exemple.acheteurservice.client;

import com.exemple.acheteurservice.dto.CreationCommandeRequestDTO;
import com.exemple.acheteurservice.dto.CommandeDetailsResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "commande-service")
public interface CommandeServiceClient {
    @PostMapping("/commandes")
    CommandeDetailsResponseDTO creerCommande(@RequestBody CreationCommandeRequestDTO request);
} 