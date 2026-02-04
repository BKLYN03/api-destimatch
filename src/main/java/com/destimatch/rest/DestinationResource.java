package com.destimatch.rest;

import com.destimatch.common.api.request.CreateDestinationRequest;
import com.destimatch.common.api.response.DestinationResponse;
import com.destimatch.service.DestinationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/destinations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DestinationResource {

    @Inject
    DestinationService destinationService;

    @POST
    public Response createDestination(CreateDestinationRequest request) {
        var destination = destinationService.createDestination(request);
        return Response.status(Response.Status.CREATED)
                .entity(destination)
                .build();
    }

    @GET
    public Response getAllDestinations(@QueryParam("query") String query) {
        List<DestinationResponse> destinations = destinationService.getAllDestinations(query);
        return Response.ok(destinations).build();
    }

    @GET
    @Path("/{id}")
    public Response getDestinationById(@PathParam("id") String id) {
        return Response.ok(destinationService.getDestinationById(id)).build();
    }
}
