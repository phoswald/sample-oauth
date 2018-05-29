package com.github.phoswald.oauth.sample.application;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import ratpack.handling.Context;

class GoogleSupport {

    private static final String CLIENT_ID = "306795211937-qnmpc5kirg6f0tv4sai4s5vna7jhlhgo.apps.googleusercontent.com";
    private static final Logger logger = Logger.getLogger(GoogleSupport.class);

    static void handleCheck(Context ctx) {
        String authorization = ctx.header("Authorization").orElse("");
        if(!authorization.toLowerCase().startsWith("bearer")) {
            logger.warn("Missing ID token.");
            ctx.clientError(400);
            //ctx.render("Missing ID token\n");
            return;
        }
        String idTokenString = authorization.substring(6).trim();
        logger.info("CheckGoogle: ID token =" + idTokenString);

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken;
        try {
            idToken = Objects.requireNonNull(verifier.verify(idTokenString));
        } catch (GeneralSecurityException | IOException e) {
            logger.error("Failed to verify ID token", e);
            ctx.clientError(500);
            //ctx.render("Failed to verify ID token\n");
            return;
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
        logger.info("- Locale: " + ((String) payload.get("locale")));
        ctx.render("email=" + payload.getEmail() + ", name=" + payload.get("name") +", id=" + payload.getSubject());
    }
}
