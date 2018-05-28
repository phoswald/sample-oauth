package com.github.phoswald.oauth.sample.application;

import org.apache.log4j.Logger;

import ratpack.handling.Context;
import ratpack.server.BaseDir;
import ratpack.server.RatpackServer;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        logger.info("oauth-sample-service is starting.");
        RatpackServer.start(server -> server
                .serverConfig(c -> {
                    c.baseDir(BaseDir.find("public/.ratpack").toAbsolutePath());
                    c.port(8080); })
                .handlers(chain -> chain
                    .get("health", Main::handleHealth)
                    .files(f -> f.indexFiles("index.html"))));
    }

    private static void handleHealth(Context ctx) {
        logger.info("Health check: OK");
        ctx.render("OK\n");
    }
}
