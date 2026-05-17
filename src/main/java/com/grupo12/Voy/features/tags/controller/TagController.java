package com.grupo12.Voy.features.tags.controller;

import com.grupo12.Voy.features.tags.ITagService;
import com.grupo12.Voy.features.tags.dto.TagsDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/tags")
public class TagController {
    private final ITagService tagService;

    @GetMapping
    ResponseEntity<List<TagsDTO>> findAll(){
        return ResponseEntity.ok(tagService.findAll());
    }

    @GetMapping("/{nameTag}")
    ResponseEntity<TagsDTO> findByName(@PathVariable String name){
        return ResponseEntity.ok(tagService.findByName(name));
    }

    @PostMapping
    ResponseEntity<TagsDTO> create(@RequestBody TagsDTO tagsDTO){
        tagService.save(tagsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{nameTag}")
    ResponseEntity<TagsDTO> update(@PathVariable String oldName,@RequestBody TagsDTO tagsDTO){
        tagService.update(oldName,tagsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    ResponseEntity<TagsDTO> delete(@RequestBody TagsDTO tagsDTO){
        tagService.delete(tagsDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
