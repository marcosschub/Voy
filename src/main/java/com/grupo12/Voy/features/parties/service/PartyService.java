package com.grupo12.Voy.features.parties.service;

import com.grupo12.Voy.common.exceptions.EntityNotFoundException;
import com.grupo12.Voy.features.parties.Dto.PartyReqDTO;
import com.grupo12.Voy.features.parties.Dto.PartyResDTO;
import com.grupo12.Voy.features.parties.PartyRepository;
import com.grupo12.Voy.features.parties.mapper.PartyMapper;
import com.grupo12.Voy.features.parties.models.PartyEntity;
import com.grupo12.Voy.features.tags.TagsRepository;
import com.grupo12.Voy.features.tags.models.TagEntity;
import com.grupo12.Voy.features.users.UserRepository;
import com.grupo12.Voy.features.users.models.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PartyService {

    private final PartyRepository partyRepository;
    private final UserRepository userRepository;
    private final PartyMapper partyMapper;
    private final TagsRepository tagsRepository;

    public List<PartyResDTO> getAll() {
        return partyMapper.toResDTOList(partyRepository.findAll());
    }

    public PartyResDTO getByExternalId(UUID id) {
        return partyRepository.findByExternalId(id)
                .map(partyMapper::toResDTO)
                .orElseThrow(()-> new EntityNotFoundException("Evento no encontrado"));
    }

    public PartyResDTO getById(Long id) {
        return partyMapper.toResDTO(partyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado")));
    }

    public List<PartyResDTO> getByOrganizer(Long organizerId) {
        List<PartyEntity> parties = partyRepository.findByOrganizerId(organizerId);
        if (parties.isEmpty()) {
            throw new EntityNotFoundException("Evento no encontrado");
        }
        return partyMapper.toResDTOList(parties);
    }

    public PartyResDTO getByTitle(String title) {
        return partyMapper.toResDTO(partyRepository.findByTitle(title.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException("Titulo no encontrado")));
    }

    public List<PartyResDTO> getByType(Boolean isPublic) {
        return partyMapper.toResDTOList(partyRepository.findByPartyAccesibility(isPublic));
    }

    public List<PartyResDTO> getByCity(String city) {
        List<PartyEntity> parties = partyRepository.findByCity(city);
        if (parties.isEmpty()) {
            throw new EntityNotFoundException("No hay eventos en esta ciudad");
        }
        return partyMapper.toResDTOList(parties);
    }

    public List<PartyResDTO> getByStatus(Boolean status) {
        List<PartyEntity> parties = partyRepository.findByState(status);
        if (parties.isEmpty()) {
            throw new EntityNotFoundException("No eventos con ese estado");
        }
        return partyMapper.toResDTOList(parties);
    }

    public PartyResDTO create(PartyReqDTO dto) {
        UserEntity organizer = userRepository.findById(dto.getIdOrganizer())
                .orElseThrow(() -> new EntityNotFoundException("Organizer no encontrado"));
        Set<TagEntity> tags = new HashSet<>(tagsRepository.findAllById(dto.getTagsIds()));

        PartyEntity party = partyMapper.toEntity(dto);
        party.setOrganizer(organizer);
        party.setTagsSet(tags);
        party.setIdExternal(UUID.randomUUID());

        return partyMapper.toResDTO(partyRepository.save(party));
    }
    public PartyResDTO update(UUID idExternal, PartyReqDTO dto) {
        PartyEntity party = partyRepository.findByExternalId(idExternal)
                        .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));

        party.setTitle(dto.getTitle());
        party.setCity(dto.getCity());
        party.setAdress(dto.getAdress());
        party.setPartyAccesibility(dto.getPartyAccesibility());
        party.setDescription(dto.getDescription());
        party.setDateTime(dto.getDateTime());
        party.setGuestLimit(dto.getGuestLimit());
        if(dto.getTagsIds() != null) {
            Set<TagEntity> tags = new HashSet<>(tagsRepository.findAllById(dto.getTagsIds()));
            party.setTagsSet(tags);
        }
        return partyMapper.toResDTO(partyRepository.save(party));
    }

    public void delete (UUID idExternal){
        PartyEntity party = partyRepository.findByExternalId(idExternal)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));
        party.setLogicState(Boolean.FALSE);
        partyRepository.delete(party);
    }

}
