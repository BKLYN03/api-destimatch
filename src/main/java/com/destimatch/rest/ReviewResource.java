package com.destimatch.rest;

import com.destimatch.common.api.request.AddReviewRequest;
import com.destimatch.service.ReviewService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/api/destinations/{id}/reviews")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReviewResource {

    @Inject
    ReviewService reviewService;
    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed("user")
    public Response getReviews(@PathParam("id") String destinationId, @QueryParam("page") @DefaultValue("0") int page) {
        return Response.ok(reviewService.getReviews(destinationId, page, 10)).build();
    }

    @POST
    @RolesAllowed("user")
    public Response addReview(@PathParam("id") String destinationId, AddReviewRequest request) {
        String email = jwt.getName();
        reviewService.addReview(email, destinationId, request);
        return Response.status(Response.Status.CREATED).entity("L'avis a bien été enrégistré!").build();
    }

    @DELETE
    @Path("/{reviewId}")
    @RolesAllowed("admin")
    public Response deleteReview(@PathParam("reviewId") String destinationId) {
        reviewService.deleteReview(destinationId);
        return Response.status(Response.Status.NO_CONTENT).entity("L'avis a bien été supprimé.").build();
    }
}
