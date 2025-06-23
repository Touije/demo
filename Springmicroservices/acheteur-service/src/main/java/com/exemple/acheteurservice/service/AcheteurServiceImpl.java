package com.exemple.acheteurservice.service;

import com.exemple.acheteurservice.client.ProduitServiceClient;
import com.exemple.acheteurservice.client.CommandeServiceClient;
import com.exemple.acheteurservice.domain.Acheteur;
import com.exemple.acheteurservice.dto.AchatRequestDTO;
import com.exemple.acheteurservice.dto.AcheteurUpdateRequestDTO;
import com.exemple.acheteurservice.dto.CreationAcheteurRequestDTO;
import com.exemple.acheteurservice.dto.ProduitDTO;
import com.exemple.acheteurservice.repository.AcheteurRepository;
import com.exemple.acheteurservice.dto.CreationCommandeRequestDTO;
import lombok.extern.slf4j.Slf4j;
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
    private final ProduitServiceClient produitServiceClient;
    private final CommandeServiceClient commandeServiceClient;

    public AcheteurServiceImpl(AcheteurRepository acheteurRepository,
                               ProduitServiceClient produitServiceClient,
                               CommandeServiceClient commandeServiceClient) {
        this.acheteurRepository = acheteurRepository;
        this.produitServiceClient = produitServiceClient;
        this.commandeServiceClient = commandeServiceClient;
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

        // 1. Valider que l'acheteur existe
        Acheteur acheteur = acheteurRepository.findById(achatRequest.getAcheteurId())
                .orElseThrow(() -> new NoSuchElementException("Acheteur non trouvé avec l'id: " + achatRequest.getAcheteurId()));

        // 2. Valider que la quantité est positive
        if (achatRequest.getQuantite() <= 0) {
            throw new IllegalArgumentException("La quantité achetée doit être supérieure à zéro.");
        }

        // 3. Valider le produit et le stock via OpenFeign
        log.debug("Appel du produit-service pour valider le produit {}", achatRequest.getProduitId());
        ProduitDTO produit;
        try {
            produit = produitServiceClient.getProduitById(achatRequest.getProduitId());
        } catch (Exception e) {
            log.error("Erreur lors de l'appel au produit-service pour le produit {}", achatRequest.getProduitId(), e);
            throw new IllegalStateException("Le service de produit est indisponible ou le produit n'existe pas.", e);
        }

        if (produit.getQuantiteEnStock() < achatRequest.getQuantite()) {
            throw new IllegalStateException("Stock insuffisant pour le produit: " + produit.getNom());
        }

        // 4. Décrémenter le stock du produit (appel synchrone)
        produitServiceClient.decrementerStock(achatRequest.getProduitId(), achatRequest.getQuantite());

        // 5. Créer la commande (appel synchrone)
        CreationCommandeRequestDTO commandeRequest = new CreationCommandeRequestDTO();
        commandeRequest.setAcheteurId(achatRequest.getAcheteurId());
        commandeRequest.setProduitId(achatRequest.getProduitId());
        commandeRequest.setQuantite(achatRequest.getQuantite());
        commandeServiceClient.creerCommande(commandeRequest);
    }
} 