package com.grupo12.Voy.features.tags;

import com.grupo12.Voy.features.tags.dto.TagsDTO;

import java.util.List;

public interface ITagsService {
    List<TagsDTO> getAll();

    TagsDTO findByName(String name);

    void save(String name);

    void delete(String name);
}
