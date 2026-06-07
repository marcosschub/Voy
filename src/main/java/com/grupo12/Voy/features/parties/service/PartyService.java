package com.grupo12.Voy.features.parties.service;

import com.grupo12.Voy.common.exceptions.AlreadyExistsException;
import com.grupo12.Voy.common.exceptions.EntityInactiveException;
import com.grupo12.Voy.common.exceptions.EntityNotFoundException;
import com.grupo12.Voy.features.parties.Dto.PartyReqDTO;
import com.grupo12.Voy.features.parties.Dto.PartyResDTO;
import com.grupo12.Voy.features.parties.repository.PartyRepository;
import com.grupo12.Voy.features.parties.mapper.PartyMapper;
import com.grupo12.Voy.features.parties.models.PartyEntity;
import com.grupo12.Voy.features.tags.TagsRepository;
import com.grupo12.Voy.features.tags.dto.TagsDTO;
import com.grupo12.Voy.features.tags.mappers.TagMapper;
import com.grupo12.Voy.features.tags.models.TagEntity;
import com.grupo12.Voy.features.users.UserRepository;
import com.grupo12.Voy.features.users.models.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PartyService implements IPartyService {

    private final PartyRepository partyRepository;
    private final UserRepository userRepository;
    private final PartyMapper partyMapper;
    private final TagsRepository tagsRepository;
    private final TagMapper tagMapper;

    @Override
    public List<PartyResDTO> getAll() {
        return partyMapper.toResDTOList(partyRepository.findAll());
    }

    @Override
    public PartyResDTO getByExternalId(UUID id) {
        return partyRepository.findByExternalId(id)
                .map(partyMapper::toResDTO)
                .orElseThrow(()-> new EntityNotFoundException("Evento no encontrado"));
    }

    @Override
    public List<PartyResDTO> getByOrganizer(UUID organizerId) {
        List<PartyEntity> parties = partyRepository.findByOrganizerId(organizerId);
        if (parties.isEmpty()) {
            throw new EntityNotFoundException("Evento no encontrado");
        }
        return partyMapper.toResDTOList(parties);
    }

    @Override
    public PartyResDTO getByTitle(String title) {
        return partyMapper.toResDTO(partyRepository.findByTitleAndPartyAccesibility(title.toUpperCase(),true)
                .orElseThrow(() -> new EntityNotFoundException("Titulo no encontrado")));
    }

    @Override
    public List<PartyResDTO> getByType(Boolean isPublic) {
        return partyMapper.toResDTOList(partyRepository.findByPartyAccesibility(isPublic));
    }

    ///devuelve solo en el caso de ser publico el evento
    @Override
    public List<PartyResDTO> getByCity(String city) {
        List<PartyEntity> parties = partyRepository.findByCityAndPartyAccesibility(city,true);
        if (parties.isEmpty()) {
            throw new EntityNotFoundException("No hay eventos en esta ciudad");
        }
        return partyMapper.toResDTOList(parties);
    }

    @Override
    public List<PartyResDTO> getByStatus(Boolean status) {
        List<PartyEntity> parties = partyRepository.findByState(status);
        if (parties.isEmpty()) {
            throw new EntityNotFoundException("No eventos con ese estado");
        }
        return partyMapper.toResDTOList(parties);
    }

    @Override
    public PartyResDTO create(PartyReqDTO dto) {
        UserEntity organizer = userRepository.findById(dto.idOrganizer())
                .orElseThrow(() -> new EntityNotFoundException("Organizer no encontrado"));
        PartyEntity party = partyMapper.toEntity(dto);
        party.setOrganizer(organizer);
        party.setExternalId(UUID.randomUUID());

        return partyMapper.toResDTO(partyRepository.save(party));
    }

    @Override
    public PartyResDTO addTag(UUID id,TagsDTO nameTag){
        PartyEntity party = partyRepository
                .findByExternalId(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));
        if(!party.getLogicState()){
            throw new EntityInactiveException("El evento se encuentra dado de baja");
        }
        TagEntity tag = tagMapper.toEntity(nameTag);

        Boolean repetido = party.getTagsList().stream()
                        .anyMatch( t -> t.getTagsId().equals(tag.getTagsId()));
        if (repetido)
            throw new AlreadyExistsException("La etiqueta ya está asignada al evento");

        party.getTagsList().add(tag);
        return partyMapper.toResDTO(partyRepository.save(party));
    }

    @Override
    public void removeTag(UUID id, TagsDTO nameTag) {
        PartyEntity party = partyRepository
                .findByExternalId(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));

        if (!party.getLogicState())
            throw new EntityInactiveException("El evento se encuentra dado de baja");

        TagEntity tag = tagMapper.toEntity(nameTag);

        boolean removed = party.getTagsList().remove(tag);
        if (!removed)
            throw new EntityNotFoundException("No se encuentra esa etiqueta en el evento");

        partyRepository.save(party);
    }

    @Override
    public PartyResDTO update(UUID idExternal, PartyReqDTO dto) {
        PartyEntity party = partyRepository.findByExternalId(idExternal)
                        .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));

        party.setTitle(dto.title());
        party.setCity(dto.city());
        party.setAdress(dto.adress());
        party.setPartyAccesibility(dto.partyAccesibility());
        party.setDescription(dto.description());
        party.setDateTime(dto.dateTime());
        party.setGuestLimit(dto.guestLimit());
        return partyMapper.toResDTO(partyRepository.save(party));
    }

    ///no elimina de la DB solo cambia el estadoLogico
    @Override
    public void delete(UUID idExternal){
        PartyEntity party = partyRepository.findByExternalId(idExternal)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));
        party.setLogicState(Boolean.FALSE);
        partyRepository.save(party);
    }

}
