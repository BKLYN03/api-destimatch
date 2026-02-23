package com.destimatch.rest;

import com.destimatch.common.api.request.LoginRequest;
import com.destimatch.common.api.request.NewUserRequest;
import com.destimatch.common.api.request.UpdatePreferencesRequest;
import com.destimatch.common.api.request.UpdateProfileRequest;
import com.destimatch.common.api.response.LoginResponse;
import com.destimatch.common.exception.ConflictException;
import com.destimatch.common.exception.ValidationException;
import com.destimatch.common.utils.ErrorInfo;
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

    @GET
    @Path("/check-admin")
    @RolesAllowed("admin")
    @Produces(MediaType.TEXT_PLAIN)
    public String checkAdminAccess() {
        return "Succès: Tu as accès à la zone Admin!";
    }

    @POST
    @Path("/auth/login")
    public Response login(LoginRequest loginRequest) {
        if (loginRequest == null)
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorInfo("Requête invalide."))
                    .build();

        try {
            String token = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
            UserEntity user = userService.getUserByEmail(loginRequest.getEmail());
            return Response.ok(new LoginResponse(token, UserConverter.toResponse(user))).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorInfo(e.getMessage()))
                .build();
        }
    }

    @GET
    @Path("/auth/refresh")
    @RolesAllowed({"user", "admin"})
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
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorInfo("Requête invalide."))
                    .build();

        try {
            var user = userService.createUser(newRequest);
            String token = Helpers.generateUserJWT(user);
            LoginResponse loginResponse = new LoginResponse(token, UserConverter.toResponse(user));

            return Response.status(Response.Status.CREATED)
                .entity(loginResponse)
                .build();
        } catch (ConflictException | ValidationException e) {
            return Response.status(Response.Status.CONFLICT)
                .entity(new ErrorInfo(e.getMessage()))
                .build();
        }
    }

    @PUT
    @Path("/profile")
    @RolesAllowed({"user", "admin"})
    public Response updateProfile(UpdateProfileRequest updateProfileRequest) {
        if (updateProfileRequest == null)
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorInfo("Requête invalide."))
                    .build();

        String email = jwt.getName();
        UserEntity updatedUser = userService.updateProfile(email, updateProfileRequest);
        return Response.ok(UserConverter.toResponse(updatedUser)).build();
    }

    @PUT
    @Path("/preferences")
    @RolesAllowed({"user", "admin"})
    public Response updatePreferences(UpdatePreferencesRequest request) {
        if (request == null)
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorInfo("Requête invalide."))
                    .build();

        try {
            String email = jwt.getName();
            userService.updateUserPreferences(email, request);
            var updatedUser = userService.getUserByEmail(email);
            return Response.ok(UserConverter.toResponse(updatedUser)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorInfo(e.getMessage()))
                .build();
        }
    }

    @GET
    @Path("/profile")
    @RolesAllowed({"user", "admin"})
    public Response getProfile() {
        String email = jwt.getName();
        var user = userService.getUserByEmail(email);
        return Response.ok(UserConverter.toResponse(user)).build();
    }

    @DELETE
    @RolesAllowed({"user", "admin"})
    public Response deleteAccount() {
        String email = jwt.getName();
        userService.deleteUser(email);
        return Response.noContent().build();
    }
}
