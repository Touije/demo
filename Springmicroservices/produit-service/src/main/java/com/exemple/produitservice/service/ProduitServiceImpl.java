package com.exemple.produitservice.service;

import com.exemple.produitservice.domain.Produit;
import com.exemple.produitservice.dto.CreationProduitRequestDTO;
import com.exemple.produitservice.dto.ProduitUpdateRequestDTO;
import com.exemple.produitservice.events.AchatEffectueEvent;
import com.exemple.produitservice.events.ProduitEvent;
import com.exemple.produitservice.repository.ProduitRepository;
import com.exemple.produitservice.config.KafkaTopicConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@Slf4j
public class ProduitServiceImpl implements ProduitServiceInterface {

    private final ProduitRepository produitRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ProduitServiceImpl(ProduitRepository produitRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.produitRepository = produitRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = KafkaTopicConfig.ACHAT_EFFECTUE_TOPIC, groupId = "produit-service-group")
    public void handleAchatEffectue(AchatEffectueEvent event) {
        log.info("Événement d'achat reçu pour déduire le stock du produit {} avec une quantité de {}", event.getProduitId(), event.getQuantite());
        try {
            decrementerStock(event.getProduitId(), event.getQuantite());
            log.info("Stock déduit avec succès pour le produit {}", event.getProduitId());
        } catch (Exception e) {
            log.error("Échec de la déduction de stock pour le produit {}: {}", event.getProduitId(), e.getMessage());
            // Ici, on pourrait publier un événement de compensation
        }
    }

    @Override
    public Produit createProduit(CreationProduitRequestDTO request) {
        Produit produit = new Produit();
        produit.setNom(request.getNom());
        produit.setPrix(request.getPrix());
        produit.setDescription(request.getDescription());
        produit.setQuantiteEnStock(request.getQuantiteEnStock());
        Produit savedProduit = produitRepository.save(produit);
        
        // Publier l'événement de création
        kafkaTemplate.send(KafkaTopicConfig.PRODUCT_EVENTS_TOPIC, new ProduitEvent(savedProduit.getId(), savedProduit.getNom(), savedProduit.getDescription(), savedProduit.getPrix(), savedProduit.getQuantiteEnStock()));
        
        return savedProduit;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produit> getProduits() {
        return StreamSupport.stream(produitRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Produit getProduitById(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Produit non trouvé avec l'id: " + id));
    }

    @Override
    public Produit updateProduit(Long id, ProduitUpdateRequestDTO request) {
        Produit produit = getProduitById(id);
        produit.setNom(request.getNom());
        produit.setDescription(request.getDescription());
        produit.setPrix(request.getPrix());
        produit.setQuantiteEnStock(request.getQuantiteEnStock());
        Produit updatedProduit = produitRepository.save(produit);

        // Publier l'événement de mise à jour
        kafkaTemplate.send(KafkaTopicConfig.PRODUCT_EVENTS_TOPIC, new ProduitEvent(updatedProduit.getId(), updatedProduit.getNom(), updatedProduit.getDescription(), updatedProduit.getPrix(), updatedProduit.getQuantiteEnStock()));
        
        return updatedProduit;
    }

    @Override
    public void deleteProduit(Long id) {
        if (!produitRepository.existsById(id)) {
            throw new NoSuchElementException("Produit non trouvé avec l'id: " + id);
        }
        produitRepository.deleteById(id);
        
        // Publier l'événement de suppression (avec un payload vide ou un marqueur)
        kafkaTemplate.send(KafkaTopicConfig.PRODUCT_EVENTS_TOPIC, String.valueOf(id), null);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Produit> findProduitById(Long id) {
        return produitRepository.findById(id);
    }

    @Override
    public void decrementerStock(Long produitId, int quantite) {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé avec l'ID: " + produitId));

        if (produit.getQuantiteEnStock() < quantite) {
            throw new IllegalStateException("Stock insuffisant pour le produit: " + produit.getNom());
        }

        produit.setQuantiteEnStock(produit.getQuantiteEnStock() - quantite);
        produitRepository.save(produit);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return produitRepository.existsById(id);
    }
} 