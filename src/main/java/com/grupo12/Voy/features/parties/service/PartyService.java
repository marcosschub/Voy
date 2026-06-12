package com.grupo12.Voy.features.parties.service;

import com.grupo12.Voy.common.exceptions.AlreadyExistsException;
import com.grupo12.Voy.common.exceptions.EntityNotFoundException;
import com.grupo12.Voy.features.parties.Dto.PartyReqDTO;
import com.grupo12.Voy.features.parties.Dto.PartyReqPrivateDto;
import com.grupo12.Voy.features.parties.Dto.PartyResDTO;
import com.grupo12.Voy.features.parties.repository.PartyRepository;
import com.grupo12.Voy.features.parties.mapper.PartyMapper;
import com.grupo12.Voy.features.parties.models.PartyEntity;
import com.grupo12.Voy.features.parties.specification.PartySpecification;
import com.grupo12.Voy.features.tags.TagsRepository;
import com.grupo12.Voy.features.tags.dto.TagsDTO;
import com.grupo12.Voy.features.tags.models.TagEntity;
import com.grupo12.Voy.features.users.UserRepository;
import com.grupo12.Voy.features.users.models.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PartyService implements IPartyService {

    private final PartyRepository partyRepository;
    private final UserRepository userRepository;
    private final PartyMapper partyMapper;
    private final TagsRepository tagsRepository;


    @Override
    public List<PartyResDTO> getAll(
            UUID partyId,
            UUID organizerId,
            String title,
            Boolean isPublic,
            String city
    ) {
        PredicateSpecification<PartyEntity> spec = PredicateSpecification.allOf(
                PartySpecification.externalIdEqual(partyId),
                PartySpecification.externalIdOrganizerEqual(organizerId),
                PartySpecification.titleContains(title),
                PartySpecification.isPublic(isPublic),
                PartySpecification.cityContains(city),
                PartySpecification.stateTrue()
        );

        return partyRepository
                .findAll(spec)
                .stream()
                .map(partyMapper::toResDTO)
                .toList();
    }

    @Override
    public PartyResDTO getByExternalId(UUID id) {
        return partyRepository.findByExternalIdAndLogicStateTrue(id)
                .map(partyMapper::toResDTO)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));
    }

    @Transactional
    @Override
    public PartyResDTO createPrivate(PartyReqPrivateDto dto) {
        UserEntity organizer = userRepository.findByExternalId(dto.idOrganizer())
                .orElseThrow(() -> new EntityNotFoundException("Organizer no encontrado"));

        if (partyRepository.existsByTitle(dto.title())) {
            throw new AlreadyExistsException("Ya existe un evento con ese titulo");
        }

        PartyEntity party = partyMapper.toEntity(dto);
        party.setPartyAccesibility(false);
        party.setPrice(BigDecimal.ZERO);
        party.setOrganizer(organizer);
        party.setExternalId(UUID.randomUUID());

        return partyMapper.toResDTO(partyRepository.save(party));
    }

    @Transactional
    @Override
    public PartyResDTO createPublic(PartyReqDTO dto) {
        UserEntity organizer = userRepository.findByExternalId(dto.idOrganizer())
                .orElseThrow(() -> new EntityNotFoundException("Organizer no encontrado"));

        if (partyRepository.existsByTitle(dto.title())) {
            throw new AlreadyExistsException("Ya existe un evento con ese titulo");
        }

        PartyEntity party = partyMapper.toEntity(dto);

        party.setPrice(dto.price());
        party.setOrganizer(organizer);
        party.setExternalId(UUID.randomUUID());

        return partyMapper.toResDTO(partyRepository.save(party));
    }

    @Override
    public PartyResDTO addTag(UUID id, TagsDTO nameTag) {
        PartyEntity party = partyRepository
                .findByExternalIdAndLogicStateTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));
        TagEntity tag = tagsRepository.findByName(nameTag.name())
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra la etiqueta " + nameTag.name()));

        Boolean repetido = party.getTagsList().stream()
                .anyMatch(t -> t.getTagsId().equals(tag.getTagsId()));
        if (repetido)
            throw new AlreadyExistsException("La etiqueta ya está asignada al evento");

        party.getTagsList().add(tag);
        return partyMapper.toResDTO(partyRepository.save(party));
    }

    @Override
    public void removeTag(UUID id, TagsDTO nameTag) {
        PartyEntity party = partyRepository
                .findByExternalIdAndLogicStateTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));

        TagEntity tag = tagsRepository.findByName(nameTag.name())
                .orElseThrow(() -> new EntityNotFoundException("La etiqueta no existe"));

        boolean removed = party.getTagsList().removeIf(t -> t.getTagsId().equals(tag.getTagsId()));
        if (!removed)
            throw new EntityNotFoundException("No se encuentra esa etiqueta en el evento");

        partyRepository.save(party);
    }

    @Override
    public PartyResDTO update(UUID idExternal, PartyReqDTO dto) {
        PartyEntity party = partyRepository.findByExternalIdAndLogicStateTrue(idExternal)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));

        if (party.getPartyAccesibility()) {
            party.setPrice(BigDecimal.ZERO);
        } else {
            party.setPrice(dto.price());
        }
        party.setTitle(dto.title());
        party.setCity(dto.city());
        party.setAdress(dto.adress());
        party.setPartyAccesibility(dto.partyAccesibility());
        party.setDescription(dto.description());
        party.setDateTime(dto.dateTime());
        party.setGuestLimit(dto.guestLimit());
        return partyMapper.toResDTO(partyRepository.save(party));
    }

    /// no elimina de la DB solo cambia el estadoLogico
    @Override
    public void delete(UUID idExternal) {
        PartyEntity party = partyRepository.findByExternalIdAndLogicStateTrue(idExternal)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));
        party.setLogicState(Boolean.FALSE);
        partyRepository.save(party);
    }

}
