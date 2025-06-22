package com.exemple.acheteurservice.service;

import com.exemple.acheteurservice.domain.Acheteur;
import com.exemple.acheteurservice.dto.AchatRequestDTO;
import com.exemple.acheteurservice.dto.AcheteurUpdateRequestDTO;
import com.exemple.acheteurservice.dto.CreationAcheteurRequestDTO;
import com.exemple.acheteurservice.events.StockADeduireEvent;
import com.exemple.acheteurservice.repository.AcheteurRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class AcheteurServiceImpl implements AcheteurServiceInterface {

    private final AcheteurRepository acheteurRepository;
    private final KafkaTemplate<String, StockADeduireEvent> kafkaTemplate;

    public AcheteurServiceImpl(AcheteurRepository acheteurRepository, KafkaTemplate<String, StockADeduireEvent> kafkaTemplate) {
        this.acheteurRepository = acheteurRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Transactional
    public Acheteur createAcheteur(CreationAcheteurRequestDTO request) {
        Acheteur acheteur = new Acheteur();
        acheteur.setNom(request.getNom());
        acheteur.setEmail(request.getEmail());
        acheteur.setAdresse(request.getAdresse());
        acheteur.setTelephone(request.getTelephone());
        return acheteurRepository.save(acheteur);
    }

    @Override
    public List<Acheteur> getAcheteurs() {
        return StreamSupport.stream(acheteurRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Acheteur getAcheteurById(Long id) {
        return acheteurRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Acheteur non trouvé avec l'id: " + id));
    }

    @Override
    public Acheteur updateAcheteur(Long id, AcheteurUpdateRequestDTO request) {
        Acheteur acheteur = getAcheteurById(id);
        acheteur.setNom(request.getNom());
        acheteur.setEmail(request.getEmail());
        acheteur.setAdresse(request.getAdresse());
        acheteur.setTelephone(request.getTelephone());
        return acheteurRepository.save(acheteur);
    }

    @Override
    public void deleteAcheteur(Long id) {
        if (!acheteurRepository.existsById(id)) {
            throw new NoSuchElementException("Acheteur non trouvé avec l'id: " + id);
        }
        acheteurRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void effectuerAchat(AchatRequestDTO achatRequest) {
        log.info("Tentative d'achat pour l'acheteur {} et le produit {}", achatRequest.getAcheteurId(), achatRequest.getProduitId());

        if (achatRequest.getQuantite() <= 0) {
            throw new IllegalArgumentException("La quantité achetée doit être supérieure à zéro.");
        }

        acheteurRepository.findById(achatRequest.getAcheteurId())
                .orElseThrow(() -> new NoSuchElementException("Acheteur non trouvé avec l'id: " + achatRequest.getAcheteurId()));

        // Publication de l'événement Kafka pour la déduction de stock
        StockADeduireEvent event = new StockADeduireEvent(achatRequest.getProduitId(), achatRequest.getQuantite());
        kafkaTemplate.send("stock-deduction-topic", event);
        log.info("Événement StockADeduireEvent publié sur Kafka pour le produit {} (quantité: {})", event.getProduitId(), event.getQuantite());
    }
} 