package com.grupo12.Voy.features.tags.service;

import com.grupo12.Voy.common.exceptions.EntityNotFoundException;
import com.grupo12.Voy.common.exceptions.InvalidFieldException;
import com.grupo12.Voy.features.tags.ITagsService;
import com.grupo12.Voy.features.tags.TagsRepository;
import com.grupo12.Voy.features.tags.dto.TagsDTO;
import com.grupo12.Voy.features.tags.models.TagsEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class TagsService implements ITagsService {
    private final TagsRepository tagsRepository;

    public List<TagsDTO> getAll(){
        return tagsRepository.findAll()
                .stream()
                .map();
    }


    public TagsEntity getByName(String nameTag){
        return tagsRepository.findByName(nameTag.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException("Etiqueta no encontrada"));
    }

    public void createTag(String nameTag){
        if (nameTag.isBlank())
            throw new InvalidFieldException("No puede estar en blanco o vacio");
        TagsEntity tag = new TagsEntity();
        tag.setName(nameTag.toUpperCase());
        ITagsRepository.save(tag);
    }

    public void deleteByName(String nameTag){
        ITagsRepository.delete(ITagsRepository.findByName(nameTag.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException("Etiqueta no encontrada")));
    }


}
