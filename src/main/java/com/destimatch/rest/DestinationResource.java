package com.destimatch.rest;

import com.destimatch.common.api.request.CreateDestinationRequest;
import com.destimatch.common.api.request.SearchCriteria;
import com.destimatch.common.api.response.DestinationResponse;
import com.destimatch.service.DestinationService;
import com.destimatch.service.MatchingService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/api/destinations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DestinationResource {

    @Inject
    DestinationService destinationService;
    @Inject
    MatchingService matchingService;
    @Inject
    JsonWebToken jwt;

    @GET
    public Response getAllDestinations() {
        try  {
            List<DestinationResponse> destinations = destinationService.getAllDestinations();
            return Response.ok(destinations).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/search")
    public Response search(@QueryParam("q") String query,
                           @QueryParam("continent") String continent,
                           @QueryParam("tag") String tag,
                           @QueryParam("style") String style,
                           @QueryParam("budget") String budget) {
        List<DestinationResponse> results =
                destinationService.searchDestinations(query, continent, tag, style, budget);
        return Response.ok(results).build();
    }

    @GET
    @Path("/{id}")
    public Response getDestinationById(@PathParam("id") String id) {
        return Response.ok(destinationService.getDestinationById(id)).build();
    }

    /* Volet ADMIN */

    @POST
    @RolesAllowed("admin")
    public Response createDestination(CreateDestinationRequest request) {
        var destination = destinationService.createDestination(request);
        return Response.status(Response.Status.CREATED)
                .entity(destination)
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response updateDestination(@PathParam("id") String id, CreateDestinationRequest request) {
        var destination = destinationService.updateDestination(id, request);
        return Response.status(Response.Status.CREATED)
                .entity(destination)
                .build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response deleteDestination(@PathParam("id") String id) {
        destinationService.deleteDestination(id);
        return Response.noContent().build();
    }

    /* Volet UTILISATEUR */

    @POST
    @Path("/match")
    @RolesAllowed("user")
    public Response searchMatches(SearchCriteria criteria) {
        String email = jwt.getName();
        return Response.ok(matchingService.findMatchesForUser(email, criteria)).build();
    }
}
