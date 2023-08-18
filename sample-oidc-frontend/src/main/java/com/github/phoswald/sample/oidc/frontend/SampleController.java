package com.github.phoswald.sample.oidc.frontend;

import java.security.Principal;

import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.oidc.IdToken;
import io.quarkus.oidc.RefreshToken;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/pages/sample")
@Authenticated
public class SampleController {

    @Inject
    Principal principal;

    /**
     * The ID token is always a JWT token and is used to represent a user
     * authentication with the JWT claims.
     */
    @Inject
    @IdToken
    JsonWebToken idToken;

    /**
     * The access token is usually used by the OIDC web application to access other
     * endpoints on behalf of the currently logged in user. <br>
     * AccessTokenCredential will have to be used if the access token issued to the
     * Quarkus web application is opaque (binary) and can not be parsed to
     * JsonWebToken.
     */
    @Inject
    JsonWebToken accessToken;

    /**
     * The refresh token is only used to refresh the ID and access tokens as part of
     * its session management process.
     */
    @Inject
    RefreshToken refreshToken;

    @Inject
    Template sample;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getSamplePage() {
        return sample.data("model", new SampleViewModel(principal, idToken, accessToken, refreshToken));
    }
}
