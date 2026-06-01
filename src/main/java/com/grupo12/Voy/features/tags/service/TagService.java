package com.grupo12.Voy.features.tags.service;

import com.grupo12.Voy.common.exceptions.EntityDuplicatedException;
import com.grupo12.Voy.common.exceptions.EntityNotFoundException;
import com.grupo12.Voy.common.exceptions.InvalidFieldException;
import com.grupo12.Voy.features.tags.ITagService;
import com.grupo12.Voy.features.tags.TagsRepository;
import com.grupo12.Voy.features.tags.dto.TagsDTO;
import com.grupo12.Voy.features.tags.mappers.TagMapper;
import com.grupo12.Voy.features.tags.models.TagEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        TagEntity tag = tagsRepository
                .findByName(name.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException("Etiqueta no encontrada"));
        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional
    public TagsDTO save(TagsDTO tagsDTO){
        if(tagsRepository
                .findAll()
                .stream()
                .anyMatch(tagEntity -> tagEntity
                        .getName()
                        .equals(tagsDTO.name().toUpperCase())))
                throw new EntityDuplicatedException("La etiqueta ya existe");
        return tagMapper.toDto(tagsRepository
                .save(tagMapper.toEntity(tagsDTO)));
    }

    @Override
    @Transactional
    public TagsDTO update(String oldname, TagsDTO tagsDTO){
        TagEntity tag = tagsRepository
                .findByName(oldname.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException("Etiqueta no encontrada"));
        tag.setName(tagsDTO.name());
        return tagMapper.toDto(tagsRepository.save(tag));
    }

    @Override
    @Transactional
    public void delete(TagsDTO tagsDTO){
        TagEntity tag = tagsRepository
                .findByName(tagsDTO.name())
                .orElseThrow(() -> new EntityNotFoundException("Etiqueta no encontrada"));
        tagsRepository.delete(tag);
    }
}
