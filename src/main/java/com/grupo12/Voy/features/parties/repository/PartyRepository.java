package com.grupo12.Voy.features.parties.repository;

import com.grupo12.Voy.features.parties.models.PartyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PartyRepository extends JpaRepository<PartyEntity,Long> {
    List<PartyEntity> findByLogicStateTrue();
    Optional<PartyEntity> findByExternalIdAndLogicStateTrue(UUID externalId);
    List<PartyEntity> findByOrganizerExternalIdAndLogicStateTrue(UUID organizerId);
    Optional<PartyEntity> findByTitleAndPartyAccesibilityAndLogicStateTrue(String title, Boolean partyAccesibility);
    List<PartyEntity> findByPartyAccesibilityAndLogicStateTrue(Boolean partyAccesibility);
    List<PartyEntity> findByCityAndPartyAccesibilityAndLogicStateTrue(String city, Boolean partyAccesibility);
    List<PartyEntity> findByStateAndLogicStateTrue(Boolean state);
    Boolean existsByTitle(String title);
}
