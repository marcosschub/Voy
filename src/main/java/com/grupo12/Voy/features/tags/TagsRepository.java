package com.grupo12.Voy.features.tags;

import com.grupo12.Voy.features.tags.dto.TagsDTO;
import com.grupo12.Voy.features.tags.models.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagsRepository extends JpaRepository<TagEntity,Long> {
    Optional<TagEntity> findByName(String name);
}
