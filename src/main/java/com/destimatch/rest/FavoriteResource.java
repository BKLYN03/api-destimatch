package com.destimatch.rest;

import com.destimatch.service.FavoriteService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/api/favorites")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FavoriteResource {

    @Inject
    JsonWebToken jwt;
    @Inject
    FavoriteService favoriteService;

    @GET
    @RolesAllowed("user")
    public Response getMyFavorites() {
        String email = jwt.getName();
        return Response.ok(favoriteService.getFavoriteDestinations(email)).build();
    }

    @POST
    @RolesAllowed("user")
    public Response addFavorite(@QueryParam("destination_id") String destinationId) {
        favoriteService.addFavorite(jwt.getName(), destinationId);
        return Response.status(Response.Status.CREATED)
                .entity("{\"message\": \"Ajouté aux favoris\"}").build();
    }

    @DELETE
    @RolesAllowed("user")
    public Response removeFavorite(@QueryParam("destination_id") String destinationId) {
        favoriteService.removeFavorite(jwt.getName(), destinationId);
        return Response.ok("{\"message\": \"Retiré des favoris\"}").build();
    }
}
