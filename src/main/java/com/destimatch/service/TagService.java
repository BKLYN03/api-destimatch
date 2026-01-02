package com.destimatch.service;

import com.destimatch.common.utils.Category;
import com.destimatch.model.TagModel;
import com.destimatch.repository.TagRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class TagService {

    @Inject
    TagRepository tagRepository;

    public List<TagModel> getAllTags() {
        return tagRepository.listAll();
    }

    public int getTagsCount() {
        return tagRepository.listAll().size();
    }

    public List<TagModel> getTagsByCategory(Category category) {
        List<TagModel> foundTags = tagRepository.find("category", category).list();

        if (foundTags.isEmpty())
            throw new NotFoundException("Aucun tag trouvé pour la catégorie: " + category);

        return foundTags;
    }
}
