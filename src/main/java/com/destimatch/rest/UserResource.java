package com.destimatch.rest;

import com.destimatch.common.api.request.LoginRequest;
import com.destimatch.common.api.request.NewUserRequest;
import com.destimatch.common.api.response.LoginResponse;
import com.destimatch.common.api.response.UserResponse;
import com.destimatch.common.exception.ValidationException;
import com.destimatch.common.utils.ErrorInfo;
import com.destimatch.common.utils.Helpers;
import com.destimatch.converter.UserConverter;
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
        return Response.ok(new LoginResponse(token)).build();
    }

    @GET
    @Path("/auth/refresh")
    @RolesAllowed({"user"})
    public Response refreshToken() {
        String email = jwt.getName();
        var user = userService.getUserByEmail(email);

        String newToken = Helpers.generateUserJWT(user);
        return Response.ok(new LoginResponse(newToken)).build();
    }

    @POST
    @Path("/register")
    public Response createUser(NewUserRequest newRequest) {
        if (newRequest == null) {
            throw new ValidationException("The request cannot be null.");
        }

        var user = userService.createUser(
                newRequest.getName(),
                newRequest.getEmail(),
                newRequest.getPassword(),
                newRequest.getLocation(),
                newRequest.getPhone()
        );

        return Response.status(Response.Status.CREATED)
                .entity(UserConverter.toResponse(user))
                .build();
    }

}
