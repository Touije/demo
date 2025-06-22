package com.exemple.acheteurservice.repository;

import com.exemple.acheteurservice.domain.Acheteur;
import org.springframework.data.repository.CrudRepository;

public interface AcheteurRepository extends CrudRepository<Acheteur, Long> {
} 