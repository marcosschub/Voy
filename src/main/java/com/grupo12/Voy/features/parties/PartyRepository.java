package com.grupo12.Voy.features.parties;

import com.grupo12.Voy.features.parties.models.PartyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyRepository extends JpaRepository<PartyEntity,Long> {
}
