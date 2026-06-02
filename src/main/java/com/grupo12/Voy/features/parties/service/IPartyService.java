package com.grupo12.Voy.features.parties.service;

import com.grupo12.Voy.features.parties.Dto.PartyReqDTO;
import com.grupo12.Voy.features.parties.Dto.PartyResDTO;

import java.util.List;
import java.util.UUID;

public interface IPartyService {
    List<PartyResDTO> getAll();

    PartyResDTO getByExternalId(UUID id);

    PartyResDTO getById(Long id);

    List<PartyResDTO> getByOrganizer(Long organizerId);

    PartyResDTO getByTitle(String title);

    List<PartyResDTO> getByType(Boolean isPublic);

    List<PartyResDTO> getByCity(String city);

    List<PartyResDTO> getByStatus(Boolean status);

    PartyResDTO create(PartyReqDTO dto);

    PartyResDTO update(UUID idExternal, PartyReqDTO dto);

    void delete(UUID idExternal);
}
