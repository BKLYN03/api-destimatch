package com.destimatch.rest;

import com.destimatch.common.api.request.LoginRequest;
import com.destimatch.common.api.request.NewUserRequest;
import com.destimatch.common.api.request.UpdateProfileRequest;
import com.destimatch.common.api.response.LoginResponse;
import com.destimatch.common.exception.ValidationException;
import com.destimatch.common.utils.Helpers;
import com.destimatch.converter.UserConverter;
import com.destimatch.entity.UserEntity;
import com.destimatch.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;
    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/auth/login")
    public Response login(LoginRequest loginRequest) {
        if (loginRequest == null)
            throw new ValidationException("Email or password invalid.");

        String token = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        UserEntity user = userService.getUserByEmail(loginRequest.getEmail());
        return Response.ok(new LoginResponse(token, UserConverter.toResponse(user))).build();
    }

    @GET
    @Path("/auth/refresh")
    @RolesAllowed("user")
    public Response refreshToken() {
        String email = jwt.getName();
        var user = userService.getUserByEmail(email);

        String newToken = Helpers.generateUserJWT(user);
        return Response.ok(new LoginResponse(newToken, UserConverter.toResponse(user))).build();
    }

    @POST
    @Path("/register")
    public Response createUser(NewUserRequest newRequest) {
        if (newRequest == null)
            throw new ValidationException("The request cannot be null.");

        var user = userService.createUser(newRequest);
        String token = Helpers.generateUserJWT(user);
        LoginResponse loginResponse = new LoginResponse(token, UserConverter.toResponse(user));

        return Response.status(Response.Status.CREATED)
                .entity(loginResponse)
                .build();
    }

    @PUT
    @Path("/profile")
    @RolesAllowed("user")
    public Response updateProfile(UpdateProfileRequest updateProfileRequest) {
        String email = jwt.getName();
        UserEntity updatedUser = userService.updateProfile(email, updateProfileRequest);
        return Response.ok(UserConverter.toResponse(updatedUser)).build();
    }

    @GET
    @Path("/profile")
    @RolesAllowed("user")
    public Response getProfile() {
        String email = jwt.getName();
        var user = userService.getUserByEmail(email);
        return Response.ok(UserConverter.toResponse(user)).build();
    }

    @POST
    @Path("/wishlist/{id}")
    @RolesAllowed("user")
    public Response addToWishlist(@PathParam("id") String destinationId) {
        String email = jwt.getName();
        userService.addToWishlist(email, destinationId);
        return Response.ok().build();
    }

    @DELETE
    @Path("/wishlist/{id}")
    @RolesAllowed("user")
    public Response removeFromWishlist(@PathParam("id") String destinationId) {
        String email = jwt.getName();
        userService.removeFromWishlist(email, destinationId);
        return Response.ok().build();
    }

    @DELETE
    @RolesAllowed("user")
    public Response deleteAccount() {
        String email = jwt.getName();
        userService.deleteUser(email);
        return Response.noContent().build();
    }
}
