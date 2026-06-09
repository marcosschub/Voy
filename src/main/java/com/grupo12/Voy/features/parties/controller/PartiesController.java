package com.grupo12.Voy.features.parties.controller;

import com.grupo12.Voy.features.parties.Dto.PartyReqDTO;
import com.grupo12.Voy.features.parties.Dto.PartyResDTO;
import com.grupo12.Voy.features.parties.service.PartyService;
import com.grupo12.Voy.features.parties.service.IPartyService;
import com.grupo12.Voy.features.tags.dto.TagsDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/parties")
public class PartiesController  {

    private final IPartyService partyService;

    @GetMapping
    public ResponseEntity<List<PartyResDTO>> findAll(@RequestParam(required = false) UUID partyId,
                                                     @RequestParam(required = false) UUID organizerId,
                                                     @RequestParam(required = false) String title,
                                                     @RequestParam(required = false) Boolean isPublic,
                                                     @RequestParam(required = false) String city
    ) {
        return ResponseEntity.ok(partyService.getAll(partyId,organizerId,title,isPublic,city));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartyResDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(partyService.getByExternalId(id));
    }

    @GetMapping("/organizer/{idOrganizer}")
    public ResponseEntity<List<PartyResDTO>> getByOrganizer(@PathVariable UUID idOrganizer) {
        return ResponseEntity.ok(partyService.getByOrganizer(idOrganizer));
    }

    /// Devuelve por titulo que sean publicos
    @GetMapping("/title/{title}")
    public ResponseEntity<PartyResDTO> getByTitle(@PathVariable String title) {
        return ResponseEntity.ok(partyService.getByTitle(title.toLowerCase()));
    }

    /// Devuelve eventos publicos
    @GetMapping("/type")
    public ResponseEntity<List<PartyResDTO>> getByType(@RequestParam Boolean isPublic) {
        return ResponseEntity.ok(partyService.getByType(isPublic));
    }

    /// Devuelve eventos en determinadas ciudades que sean publicos
    @GetMapping("/city")
    public ResponseEntity<List<PartyResDTO>> getByCity(@RequestParam String city) {
        return ResponseEntity.ok(partyService.getByCity(city));
    }

    /// devuelve eventos activos, publicos y privados (VER!)
    @GetMapping("/status")
    public ResponseEntity<List<PartyResDTO>> getByStatus(@RequestParam Boolean status) {
        return ResponseEntity.ok(partyService.getByStatus(status));
    }

    /// crea nuevo evento
    @PostMapping
    public ResponseEntity<PartyResDTO> create(@RequestBody @Valid PartyReqDTO party) {
        return ResponseEntity.status(HttpStatus.CREATED).body(partyService.create(party));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartyResDTO> update(@PathVariable UUID id,@RequestBody @Valid PartyReqDTO party) {
        return ResponseEntity.ok(partyService.update(id, party));
    }

    @PostMapping("/{id}/tag")
    public ResponseEntity<PartyResDTO> addTag(@PathVariable UUID id, @RequestBody @Valid TagsDTO tagsDTO){
        return ResponseEntity.ok(partyService.addTag(id,tagsDTO));
    }

    @DeleteMapping("/{id}/tag")
    public ResponseEntity<Void> removeTag(@PathVariable UUID id, @RequestBody @Valid TagsDTO tagsDTO){
        partyService.removeTag(id,tagsDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        partyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
