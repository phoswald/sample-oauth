package com.github.phoswald.oauth.sample.application;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.net.ssl.SSLException;

import org.apache.log4j.Logger;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import ratpack.handling.Context;
import ratpack.server.BaseDir;
import ratpack.server.RatpackServer;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        Arguments arguments = new Arguments("app", args);
        boolean enableSsl = arguments.getBoolean("enable-ssl").orElse(Boolean.FALSE);
        int port = arguments.getInteger("port").orElse(enableSsl ? 8443 : 8080);
        logger.info("Serving " + (enableSsl ? "https://" : "http://") + "localhost:" + port);
        RatpackServer.start(server -> server
                .serverConfig(c -> {
                    c.baseDir(BaseDir.find("public/.ratpack").toAbsolutePath());
                    c.port(port);
                    if(enableSsl) {
                        c.ssl(sslContext(arguments));
                    }
                })
                .handlers(chain -> chain
                    .get("health", Main::handleHealth)
                    .get("check-google", GoogleSupport::handleCheck)
                    .files(f -> f.indexFiles("index.html"))));
    }

    private static SslContext sslContext(Arguments arguments) {
        try {
            Path chainFile = arguments.getPath("ssl-chain-file").orElse(Paths.get("fullchain.pem"));
            Path keyFile = arguments.getPath("ssl-key-file").orElse(Paths.get("privkey.pem"));
            logger.info("Using SSL certificate chain file: " + chainFile);
            logger.info("Using SSL private key file: " + keyFile);
            return SslContextBuilder.forServer(chainFile.toFile(), keyFile.toFile()).build();
        } catch (SSLException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void handleHealth(Context ctx) {
        logger.info("Health: OK");
        ctx.render("OK\n");
    }
}
