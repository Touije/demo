package com.exemple.commandeservice.service;

import com.exemple.commandeservice.domain.Commande;
import com.exemple.commandeservice.domain.StatutCommande;
import com.exemple.commandeservice.dto.AcheteurDTO;
import com.exemple.commandeservice.dto.CommandeDetailsResponseDTO;
import com.exemple.commandeservice.dto.ProduitDTO;
import com.exemple.commandeservice.dto.CreationCommandeRequestDTO;
import com.exemple.commandeservice.client.AcheteurServiceClient;
import com.exemple.commandeservice.client.ProduitServiceClient;
import com.exemple.commandeservice.mapper.CommandeMapper;
import com.exemple.commandeservice.repository.CommandeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@Slf4j
public class CommandeServiceImpl implements CommandeServiceInterface {

    private final CommandeRepository commandeRepository;
    private final CommandeMapper commandeMapper;
    private final AcheteurServiceClient acheteurServiceClient;
    private final ProduitServiceClient produitServiceClient;

    public CommandeServiceImpl(CommandeRepository commandeRepository, CommandeMapper commandeMapper,
                               AcheteurServiceClient acheteurServiceClient, ProduitServiceClient produitServiceClient) {
        this.commandeRepository = commandeRepository;
        this.commandeMapper = commandeMapper;
        this.acheteurServiceClient = acheteurServiceClient;
        this.produitServiceClient = produitServiceClient;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandeDetailsResponseDTO> getAllCommandesWithDetails() {
        return StreamSupport.stream(commandeRepository.findAll().spliterator(), false)
                .map(this::mapToDetailsDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommandeDetailsResponseDTO getCommandeById(Long id) {
        log.info("Récupération de la commande avec l'ID: {}", id);
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Commande non trouvée avec l'id: " + id));
        return mapToDetailsDto(commande);
    }

    private CommandeDetailsResponseDTO mapToDetailsDto(Commande commande) {
        // Appels synchrones via Feign pour récupérer les détails
        AcheteurDTO acheteur = acheteurServiceClient.getAcheteurById(commande.getAcheteurId());
        ProduitDTO produit = produitServiceClient.getProduitById(commande.getProduitId());
        return commandeMapper.toDetailsDto(commande, acheteur, produit);
    }

    @Override
    public void deleteCommande(Long id) {
        if (!commandeRepository.existsById(id)) {
            throw new NoSuchElementException("Commande non trouvée avec l'id: " + id);
        }
        commandeRepository.deleteById(id);
    }

    @Override
    public void changerStatut(Long commandeId, StatutCommande nouveauStatut) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new NoSuchElementException("Commande non trouvée avec l'id: " + commandeId));
        commande.setStatut(nouveauStatut);
        commandeRepository.save(commande);
    }

    @Override
    public CommandeDetailsResponseDTO creerCommande(CreationCommandeRequestDTO request) {
        // Vérifier l'acheteur
        AcheteurDTO acheteur = acheteurServiceClient.getAcheteurById(request.getAcheteurId());
        // Vérifier le produit
        ProduitDTO produit = produitServiceClient.getProduitById(request.getProduitId());
        // Créer la commande
        Commande commande = new Commande();
        commande.setAcheteurId(request.getAcheteurId());
        commande.setProduitId(request.getProduitId());
        commande.setQuantite(request.getQuantite());
        commande.setStatut(StatutCommande.EN_COURS);
        commande.setDateCommande(java.time.LocalDateTime.now());
        commandeRepository.save(commande);
        return mapToDetailsDto(commande);
    }
} 