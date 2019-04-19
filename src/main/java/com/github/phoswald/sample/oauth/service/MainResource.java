package com.github.phoswald.sample.oauth.service;

import java.time.LocalDateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class MainResource {

    private final Logger logger = Logger.getLogger(getClass());

    @GET
    @Path("/health")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getHealth() {
        logger.info("Health: OK");
        return Response.ok("OK\n").build();
    }

    @GET
    @Path("/time")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getTime() {
        String response = "The current time is " + LocalDateTime.now();
        logger.info(response);
        return Response.ok(response + "\n").build();
    }
}
