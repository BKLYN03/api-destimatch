package com.destimatch.rest.mapper;

import com.destimatch.common.exception.ConflictException;
import com.destimatch.common.exception.ValidationException;
import com.destimatch.common.utils.ErrorInfo;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

public class GlobalExceptionMapper {

    @ServerExceptionMapper
    public Response handleValidation(ValidationException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorInfo(e.getMessage())).build();
    }

    @ServerExceptionMapper
    public Response handleConflict(ConflictException e) {
        return Response.status(Response.Status.CONFLICT).entity(new ErrorInfo(e.getMessage())).build();
    }

    @ServerExceptionMapper
    public Response handleUnauthorized(NotAuthorizedException e) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorInfo(e.getMessage())).build();
    }

    @ServerExceptionMapper
    public Response handleNotFound(NotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND).entity(new ErrorInfo(e.getMessage())).build();
    }

    @ServerExceptionMapper
    public Response handleFallBack(Throwable e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorInfo("An error occurred."))
                .build();
    }
}
