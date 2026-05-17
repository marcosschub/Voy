package com.grupo12.Voy.features.parties;

import com.grupo12.Voy.features.parties.models.PartyEntity;
import com.grupo12.Voy.features.users.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PartyRepository extends JpaRepository<PartyEntity,Long> {
    List<PartyEntity> findByOrganizerId(Long organizerId);
    Optional <PartyEntity> findByExternalId(UUID id);
    List<PartyEntity> findByPartyAccesibility(Boolean partyAccesibility);
    Optional<PartyEntity> findByTitle(String title);
    List<PartyEntity> findByCity(String city);
    List<PartyEntity> findByState(Boolean state);
}
