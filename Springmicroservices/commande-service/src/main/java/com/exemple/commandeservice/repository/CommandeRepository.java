package com.exemple.commandeservice.repository;

import com.exemple.commandeservice.domain.Commande;
import org.springframework.data.repository.CrudRepository;

public interface CommandeRepository extends CrudRepository<Commande, Long> {
} 