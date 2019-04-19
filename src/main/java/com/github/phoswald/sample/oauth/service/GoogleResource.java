package com.github.phoswald.sample.oauth.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Objects;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

@Path("/google")
public class GoogleResource {

    private static final String CLIENT_ID = "306795211937-qnmpc5kirg6f0tv4sai4s5vna7jhlhgo.apps.googleusercontent.com";

    private final Logger logger = Logger.getLogger(getClass());

    @GET
    @Path("/check")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getCheck(@QueryParam("token") String idTokenString) {
        logger.info("CheckGoogle: ID token =" + idTokenString);

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken;
        try {
            idToken = Objects.requireNonNull(verifier.verify(idTokenString));
        } catch (GeneralSecurityException | IOException e) {
            logger.error("Failed to verify ID token", e);
            // entity("Failed to verify ID token\n")
            return Response.serverError().build();
        }

        Payload payload = idToken.getPayload();
        logger.info("Authenticated with Google:");
        logger.info("- User ID: " + payload.getSubject());
        logger.info("- Email: " + payload.getEmail());
        logger.info("- Email verified: " + payload.getEmailVerified());
        logger.info("- Name: " + ((String) payload.get("name")));
        logger.info("- Given Name: " + ((String) payload.get("given_name")));
        logger.info("- Family Name: " + ((String) payload.get("family_name")));
        logger.info("- Image URL: " + ((String) payload.get("picture")));
        String response = "email=" + payload.getEmail() + ", name=" + payload.get("name") +", id=" + payload.getSubject();
        return Response.ok(response).build();
    }
}
