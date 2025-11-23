package com.learn.blog.services;

import com.learn.blog.domain.entities.Tag;
import com.learn.blog.repositories.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public List<Tag> getTags() {
        return tagRepository.findAllWithPostCount();
    }

    @Transactional
    public List<Tag> createTags(Set<String> names) {
        List<Tag> existingTags = tagRepository.findByNameIn(names);
        Set<String> existingNames = existingTags.stream().map(tag ->
                tag.getName()).collect(Collectors.toSet());
        List<Tag> newTags = names.stream().filter(name -> !existingNames.contains(name))
                .map(name -> Tag.builder()
                        .name(name)
                        .posts(new HashSet<>())
                        .build()).toList();
        List<Tag> savedTags = new ArrayList<>();
        if (!newTags.isEmpty()) {
            savedTags =  tagRepository.saveAll(newTags);
        }
        savedTags.addAll(existingTags);
        return savedTags;
    }

    public void deleteTag(UUID id) {
        tagRepository.findById(id).ifPresent(tag -> {
            if (!tag.getPosts().isEmpty()) {
                throw new IllegalStateException("Cannot delete posts because of existing tag");
            }
            tagRepository.delete(tag);
        });
    }

    public Tag getById(UUID tagId) {
        return  tagRepository.findById(tagId).orElseThrow(() -> new EntityNotFoundException("No tag found with id: " + tagId));
    }

    public List<Tag> getByIds(Set<UUID> tagsId) {
        List<Tag> tags = tagRepository.findAllById(tagsId);
        if (tags.size() != tagsId.size()) {
            throw new EntityNotFoundException("No tags found with ids: " + tagsId);
        }
        return tags;
    }
}
