package com.destimatch.rest;

import com.destimatch.common.utils.ErrorInfo;
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
    @Path("/most-liked-continents")
    public Response getMostLikedContinentsDesc() {
        try {
            return Response.ok(favoriteService.getTopContinents()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorInfo(e.getMessage()))
                .build();
        }
    }

    @GET
    @RolesAllowed("user")
    public Response getMyFavorites() {
        String email = jwt.getName();
        return Response.ok(favoriteService.getFavoriteDestinations(email)).build();
    }

    @POST
    @RolesAllowed("user")
    public Response addFavorite(@QueryParam("destination_id") String destinationId) {
        try {
            favoriteService.addFavorite(jwt.getName(), destinationId);
            return Response.status(Response.Status.CREATED)
                .entity("{\"message\": \"Ajouté aux favoris\"}")
                .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorInfo(e.getMessage()))
                .build();
        }
    }

    @DELETE
    @RolesAllowed("user")
    public Response removeFavorite(@QueryParam("destination_id") String destinationId) {
        try {
            favoriteService.removeFavorite(jwt.getName(), destinationId);
            return Response.status(Response.Status.NO_CONTENT)
                .entity("{\"message\": \"Retiré des favoris\"}")
                .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorInfo(e.getMessage()))
                .build();
        }
    }
}
