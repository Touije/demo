package com.exemple.commandeservice.api;

import com.exemple.commandeservice.dto.ChangementStatutRequestDTO;
import com.exemple.commandeservice.dto.CommandeDetailsResponseDTO;
import com.exemple.commandeservice.dto.CreationCommandeRequestDTO;
import com.exemple.commandeservice.service.CommandeServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commandes")
public class CommandeController {

    private final CommandeServiceInterface commandeService;

    public CommandeController(CommandeServiceInterface commandeService) {
        this.commandeService = commandeService;
    }

    @GetMapping
    public List<CommandeDetailsResponseDTO> listerCommandes() {
        return commandeService.getAllCommandesWithDetails();
    }

    @GetMapping("/{id}")
    public CommandeDetailsResponseDTO getCommandeById(@PathVariable Long id) {
        return commandeService.getCommandeById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommande(@PathVariable Long id) {
        commandeService.deleteCommande(id);
    }

    @PutMapping("/{commandeId}/statut")
    @ResponseStatus(HttpStatus.OK)
    public void changerStatut(@PathVariable Long commandeId, @RequestBody ChangementStatutRequestDTO request) {
        commandeService.changerStatut(commandeId, request.getNouveauStatut());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommandeDetailsResponseDTO creerCommande(@RequestBody CreationCommandeRequestDTO request) {
        return commandeService.creerCommande(request);
    }
} 