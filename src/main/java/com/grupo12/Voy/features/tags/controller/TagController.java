package com.grupo12.Voy.features.tags.controller;

import com.grupo12.Voy.features.tags.ITagService;
import com.grupo12.Voy.features.tags.dto.TagsDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/tags")
public class TagController {
    private final ITagService tagService;

    @GetMapping
    ResponseEntity<List<TagsDTO>> findAll(@RequestParam(required = false) String name){
        return ResponseEntity.ok(tagService.findAll(name));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<TagsDTO> create(@RequestBody @Valid TagsDTO tagsDTO){
        tagService.save(tagsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(tagsDTO);
    }

    @PutMapping("/{oldName}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<TagsDTO> update(@PathVariable String oldName,@RequestBody @Valid TagsDTO tagsDTO){
        tagService.update(oldName,tagsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<TagsDTO> delete(@RequestBody @Valid TagsDTO tagsDTO){
        tagService.delete(tagsDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
