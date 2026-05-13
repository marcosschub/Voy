package com.grupo12.Voy.features.tags;

import com.grupo12.Voy.features.tags.dto.TagsDTO;

import java.util.List;

public interface ITagService {
    List<TagsDTO> findAll();

    TagsDTO findByName(String name);

    void save(TagsDTO tagsDTO);

    void delete(TagsDTO tagsDTO);
}
