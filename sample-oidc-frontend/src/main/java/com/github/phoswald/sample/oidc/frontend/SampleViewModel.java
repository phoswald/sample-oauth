package com.github.phoswald.sample.oidc.frontend;

import java.security.Principal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.oidc.RefreshToken;

public class SampleViewModel {

    public final String name;
    public final String currentTime;
    public final JsonWebTokenViewModel idToken;
    public final JsonWebTokenViewModel accessToken;
    public final String refreshToken;

    public SampleViewModel(Principal principal, JsonWebToken idToken, JsonWebToken accessToken, RefreshToken refreshToken) {
        this.name = principal.getName();
        this.currentTime = formatDateTime(System.currentTimeMillis() / 1000);
        this.idToken = new JsonWebTokenViewModel(idToken);
        this.accessToken = new JsonWebTokenViewModel(accessToken);
        this.refreshToken = refreshToken.getToken();
    }

    public static class JsonWebTokenViewModel {

        public final String name;
        public final String subject;
        public final String audience;
        public final String issuer;
        public final String issuedAt;
        public final String expiration;
        public final String groups;
        public final String preferredUsername;
        public final String scope;
        public final String tokenId;
        public final String claimNames;
        public final String rawToken;

        public JsonWebTokenViewModel(JsonWebToken token) {
            this.name = token.getName();
            this.subject = token.getSubject();
            this.audience = formatSet(token.getAudience());
            this.issuer = token.getIssuer();
            this.issuedAt = formatDateTime(token.getIssuedAtTime());
            this.expiration = formatDateTime(token.getExpirationTime());
            this.groups = formatSet(token.getGroups());
            this.preferredUsername = token.getClaim("preferred_username");
            this.scope = token.getClaim("scope");
            this.tokenId = token.getTokenID();
            this.claimNames = formatSet(token.getClaimNames());
            this.rawToken = token.getRawToken();
        }
    }

    private static String formatSet(Set<?> set) {
        return Optional.ofNullable(set).orElse(Collections.emptySet()).toString();
    }

    private static String formatDateTime(long epoch) {
        return Instant.ofEpochSecond(epoch) //
                .atZone(ZoneId.systemDefault()) //
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
