package com.grupo12.Voy.features.tags.service;

import com.grupo12.Voy.common.exceptions.EntityDuplicatedException;
import com.grupo12.Voy.common.exceptions.EntityNotFoundException;
import com.grupo12.Voy.features.tags.ITagService;
import com.grupo12.Voy.features.tags.TagsRepository;
import com.grupo12.Voy.features.tags.dto.TagsDTO;
import com.grupo12.Voy.features.tags.mappers.TagMapper;
import com.grupo12.Voy.features.tags.models.TagEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class TagService implements ITagService {
    private final TagsRepository tagsRepository;

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagsDTO> findAll(){
        return tagsRepository
                .findAll()
                .stream()
                .map(tagMapper::toDto)
                .toList();
    }

    @Override
    public TagsDTO findByName(String name){
        TagEntity tag = tagMapper.toEntity(tagsRepository
                .findByName(name.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException("Etiqueta no encontrada")));
        return tagMapper.toDto(tag);
    }

    @Override
    public TagsDTO save(TagsDTO tagsDTO){
        tagsRepository
                .findByName(tagsDTO.getName())
                .orElseThrow(() -> new EntityDuplicatedException("La etiqueta ya existe"));
        tagsDTO.setName(tagsDTO.getName());
        return tagMapper.toDto(tagsRepository
                .save(tagMapper.toEntity(tagsDTO)));
    }

    @Override
    public TagsDTO update(String oldname, TagsDTO tagsDTO){
        TagEntity tag = tagMapper
                            .toEntity(tagsRepository.findByName(oldname)
                            .orElseThrow(() -> new EntityNotFoundException("Etiqueta no encontrada")));
        tag.setName(tagsDTO.getName());
        tagsRepository.save(tag);
        return tagMapper.toDto(tag);
    }

    @Override
    public void delete(TagsDTO tagsDTO){
        TagEntity tag = tagMapper
                            .toEntity(tagsRepository.findByName(tagsDTO.getName().toUpperCase())
                            .orElseThrow(() -> new EntityNotFoundException("Etiqueta no encontrada")));
        tagsRepository.delete(tag);
    }
}
