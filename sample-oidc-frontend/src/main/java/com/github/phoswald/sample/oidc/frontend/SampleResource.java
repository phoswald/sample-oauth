package com.github.phoswald.sample.oidc.frontend;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.oidc.IdToken;
import io.quarkus.oidc.RefreshToken;
import io.quarkus.security.Authenticated;

@Path("/sample")
@Authenticated
public class SampleResource {
    
    /**
     * The ID token is always a JWT token and is used to represent a user authentication with the JWT claims.
     */
    @Inject
    @IdToken
    JsonWebToken idToken;

    /**
     * The access token is usually used by the OIDC web application to access other endpoints
     * on behalf of the currently logged in user.
     * <br>
     * AccessTokenCredential will have to be used if the access token issued to the 
     * Quarkus web application is opaque (binary) and can not be parsed to JsonWebToken.
     */
    @Inject
    JsonWebToken accessToken;

    /**
     * The refresh token is only used to refresh the ID and access tokens as part of its session management process.
     */
    @Inject
    RefreshToken refreshToken;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getSamplePage() {
        return "<!doctype html>\n" //
                + "<html><body>" //
                + "<h1>Hello, " + idToken.getName() + "!</h1>\n" //
                + "<h2>ID Token</h2>\n" //
                + "<p>ID Token: name: " + idToken.getName() + "</p>\n" //
                + "<p>ID Token: issuer / subject: " + idToken.getIssuer() + " / " + idToken.getSubject() + "</p>\n" //
                + "<p>ID Token: groups: " + idToken.getGroups() + "</p>\n" //
                + "<p>ID Token: claim 'preferred_username': " + idToken.getClaim("preferred_username") + "</p>\n" //
//              + "<p>ID Token: claim 'scope': " + idToken.getClaim("scope") + "</p>\n" //
                + "<pre>" + idToken.getRawToken() + "</pre>\n" //
                + "<h2>Access Token</h2>\n" //
                + "<p>Access Token: name: " + accessToken.getName() + "</p>\n" //
                + "<p>Access Token: issuer / subject: " + accessToken.getIssuer() + " / " + accessToken.getSubject() + "</p>\n" //
                + "<p>Access Token: groups: " + accessToken.getGroups() + "</p>\n" //
                + "<p>Access Token: claim 'preferred_username': " + accessToken.getClaim("preferred_username") + "</p>\n" //
                + "<p>Access Token: claim 'scope': " + accessToken.getClaim("scope") + "</p>\n" //
                + "<pre>" + accessToken.getRawToken() + "</pre>\n" //
                + "<h2>Refresh Token</h2>\n" //
                + "<pre>" + refreshToken.getToken() + "</pre>\n" //
                + "</body></html>"; //
    }
}
