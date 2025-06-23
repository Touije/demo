package com.exemple.produitservice.api;

import com.exemple.produitservice.dto.CreationProduitRequestDTO;
import com.exemple.produitservice.dto.ProduitResponseDTO;
import com.exemple.produitservice.dto.ProduitUpdateRequestDTO;
import com.exemple.produitservice.mapper.ProduitMapper;
import com.exemple.produitservice.service.ProduitServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produits")
public class ProduitController {

    private final ProduitServiceInterface produitService;
    private final ProduitMapper produitMapper;

    public ProduitController(ProduitServiceInterface produitService, ProduitMapper produitMapper) {
        this.produitService = produitService;
        this.produitMapper = produitMapper;
    }

    @PostMapping
    public ProduitResponseDTO createProduit(@RequestBody CreationProduitRequestDTO request) {
        return produitMapper.toDto(produitService.createProduit(request));
    }

    @GetMapping
    public List<ProduitResponseDTO> getProduits() {
        return produitService.getProduits().stream()
                .map(produitMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProduitResponseDTO getProduitById(@PathVariable Long id) {
        return produitMapper.toDto(produitService.getProduitById(id));
    }

    @PutMapping("/{id}")
    public ProduitResponseDTO updateProduit(@PathVariable Long id, @RequestBody ProduitUpdateRequestDTO request) {
        return produitMapper.toDto(produitService.updateProduit(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduit(@PathVariable Long id) {
        produitService.deleteProduit(id);
    }

    @PostMapping("/{id}/decrementer-stock")
    @ResponseStatus(HttpStatus.OK)
    public void decrementerStock(@PathVariable Long id, @RequestParam int quantite) {
        produitService.decrementerStock(id, quantite);
    }
} 