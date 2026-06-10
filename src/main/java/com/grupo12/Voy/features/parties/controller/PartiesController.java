package com.grupo12.Voy.features.parties.controller;

import com.grupo12.Voy.features.parties.Dto.PartyReqDTO;
import com.grupo12.Voy.features.parties.Dto.PartyReqPrivateDto;
import com.grupo12.Voy.features.parties.Dto.PartyResDTO;
import com.grupo12.Voy.features.parties.service.PartyService;
import com.grupo12.Voy.features.parties.service.IPartyService;
import com.grupo12.Voy.features.tags.dto.TagsDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    /// crea nuevo evento
    @PostMapping("/public")
    @PreAuthorize("hasRole('ROLE_ORGANIZATOR')")
    public ResponseEntity<PartyResDTO> createPublic(@RequestBody @Valid PartyReqDTO party) {
        return ResponseEntity.status(HttpStatus.CREATED).body(partyService.createPublic(party));
    }

    @PostMapping("/private")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<PartyResDTO> createPrivate(@RequestBody @Valid PartyReqPrivateDto party) {
        return ResponseEntity.status(HttpStatus.CREATED).body(partyService.createPrivate(party));
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
