package com.exemple.produitservice.repository;

import com.exemple.produitservice.domain.Produit;
import org.springframework.data.repository.CrudRepository;

public interface ProduitRepository extends CrudRepository<Produit, Long> {
} 