package com.exemple.acheteurservice.api;

import com.exemple.acheteurservice.dto.AchatRequestDTO;
import com.exemple.acheteurservice.dto.AcheteurResponseDTO;
import com.exemple.acheteurservice.dto.AcheteurUpdateRequestDTO;
import com.exemple.acheteurservice.dto.CreationAcheteurRequestDTO;
import com.exemple.acheteurservice.mapper.AcheteurMapper;
import com.exemple.acheteurservice.service.AcheteurServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/acheteurs")
public class AcheteurController {

    private final AcheteurServiceInterface acheteurService;
    private final AcheteurMapper acheteurMapper;

    public AcheteurController(AcheteurServiceInterface acheteurService, AcheteurMapper acheteurMapper) {
        this.acheteurService = acheteurService;
        this.acheteurMapper = acheteurMapper;
    }

    @PostMapping
    public AcheteurResponseDTO createAcheteur(@RequestBody CreationAcheteurRequestDTO acheteur) {
        return acheteurMapper.toDto(acheteurService.createAcheteur(acheteur));
    }

    @GetMapping
    public List<AcheteurResponseDTO> getAcheteurs() {
        return acheteurService.getAcheteurs().stream()
                .map(acheteurMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AcheteurResponseDTO getAcheteurById(@PathVariable Long id) {
        return acheteurMapper.toDto(acheteurService.getAcheteurById(id));
    }

    @PutMapping("/{id}")
    public AcheteurResponseDTO updateAcheteur(@PathVariable Long id, @RequestBody AcheteurUpdateRequestDTO request) {
        return acheteurMapper.toDto(acheteurService.updateAcheteur(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAcheteur(@PathVariable Long id) {
        acheteurService.deleteAcheteur(id);
    }

    @PostMapping("/achat")
    public void effectuerAchat(@RequestBody AchatRequestDTO achatRequest) {
        acheteurService.effectuerAchat(achatRequest);
    }
} 