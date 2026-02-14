package com.destimatch.rest;

import com.destimatch.service.DestinationService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/tags")
@Produces(MediaType.APPLICATION_JSON)
public class TagResource {

    @Inject
    DestinationService destinationService;

    @GET
    @PermitAll
    public Response getAllTags() {
        List<String> tags = destinationService.getAvailableTags();
        return Response.ok(tags).build();
    }
}
