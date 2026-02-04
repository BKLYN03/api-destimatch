package com.destimatch.service;

import com.destimatch.common.api.response.TagResponse;
import com.destimatch.common.utils.Category;
import com.destimatch.converter.TagConverter;
import com.destimatch.entity.TagEntity;
import com.destimatch.repository.TagRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TagService {

    @Inject
    TagRepository tagRepository;

    public List<TagResponse> getAllTags() {
        return tagRepository.listAll().stream()
                .map(TagConverter::toResponse)
                .collect(Collectors.toList());
    }

    public long getTagsCount() {
        return tagRepository.count();
    }

    public List<TagResponse> getTagsByCategory(Category category) {
        return tagRepository.list("category", category)
                .stream().map(TagConverter::toResponse)
                .collect(Collectors.toList());
    }
}
