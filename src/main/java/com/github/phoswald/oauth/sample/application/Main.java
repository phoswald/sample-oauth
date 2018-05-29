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
import ratpack.server.BaseDir;
import ratpack.server.RatpackServer;

public class Main {

    private static final String CLIENT_ID = "306795211937-qnmpc5kirg6f0tv4sai4s5vna7jhlhgo.apps.googleusercontent.com";
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        logger.info("oauth-sample-service is starting.");
        RatpackServer.start(server -> server
                .serverConfig(c -> {
                    c.baseDir(BaseDir.find("public/.ratpack").toAbsolutePath());
                    c.port(8080); })
                .handlers(chain -> chain
                    .get("health", Main::handleHealth)
                    .get("check-google", Main::handleCheckGoogle)
                    .files(f -> f.indexFiles("index.html"))));
    }

    private static void handleHealth(Context ctx) {
        logger.info("Health: OK");
        ctx.render("OK\n");
    }

    private static void handleCheckGoogle(Context ctx) {
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

        StringBuilder sb = new StringBuilder();
        sb.append("- User ID: " + payload.getSubject() + "\n");
        sb.append("- Email: " + payload.getEmail() + "\n");
        sb.append("- Email verified: " + payload.getEmailVerified() + "\n");
        sb.append("- Name: " + ((String) payload.get("name")) + "\n");
        sb.append("- Given Name: " + ((String) payload.get("given_name")) + "\n");
        sb.append("- Family Name: " + ((String) payload.get("family_name")) + "\n");
        sb.append("- Image URL: " + ((String) payload.get("picture")) + "\n");
        sb.append("- Locale: " + ((String) payload.get("locale")) + "\n");

        logger.info("Authenticated with Google:\n" + sb.toString());
        ctx.render(sb.toString());
    }
}