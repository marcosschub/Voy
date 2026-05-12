package com.grupo12.Voy.features.tags;

import com.grupo12.Voy.features.tags.models.TagsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagsRepository extends JpaRepository<TagsEntity,Long> {
    Optional<TagsEntity> findByName(String name);
}
