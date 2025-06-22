package com.exemple.commandeservice.client;

import com.exemple.commandeservice.dto.AcheteurDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "acheteur-service")
public interface AcheteurServiceClient {

    @GetMapping("/acheteurs/{id}")
    AcheteurDTO getAcheteurById(@PathVariable("id") Long id);
} 