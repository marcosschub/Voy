package com.grupo12.Voy.features.parties.repository;

import com.grupo12.Voy.features.parties.models.PartyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PartyRepository extends JpaRepository<PartyEntity,Long> {
    List<PartyEntity> findByOrganizerId(UUID organizerId);
    Optional <PartyEntity> findByExternalId(UUID id);
    List<PartyEntity> findByPartyAccesibility(Boolean partyAccesibility);
    Optional<PartyEntity> findByTitleAndPartyAccesibility(String title, Boolean isPublic);
    List<PartyEntity> findByCityAndPartyAccesibility(String city, Boolean isPublic);
    List<PartyEntity> findByState(Boolean state);
}
