package com.github.phoswald.sample.oidc.frontend;

import java.security.Principal;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/rest/sample")
@Authenticated
public class SampleResource {

    @Inject
    Principal principal;

    @GET
    @Path("/me")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getConfig() {
        return Response.ok(principal.getName()).build();
    }
}
