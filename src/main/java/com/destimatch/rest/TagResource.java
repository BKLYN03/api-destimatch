package com.destimatch.rest;

import com.destimatch.common.utils.Category;
import com.destimatch.model.TagModel;
import com.destimatch.service.TagService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/tags")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TagResource {

    @Inject
    TagService tagService;

//    @GET
//    public Response getAllTags() {
//        List<TagModel> tags = tagService.getAllTags();
//        return Response.ok(tags).build();
//    }

    @GET
    @Path("/count")
    public Response getTagsNumber() {
        int count = tagService.getTagsCount();
        return Response.ok(count).build();
    }

    @GET
    public Response getTags(@QueryParam("category") String categoryStr) {
        if (categoryStr == null || categoryStr.isEmpty())
            return Response.ok(tagService.getAllTags()).build();

        try {
            Category category = Category.valueOf(categoryStr.toUpperCase());
            List<TagModel> tags = tagService.getTagsByCategory(category);
            return Response.ok(tags).build();
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("La catégorie '" + categoryStr + "' n'est pas valide.");
        }
    }
}
