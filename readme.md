# oauth-sample-service

Build:

    $ mvn clean verify
    $ ./run.sh
    
Docker:

    $ mvn clean verify -P docker
    $ docker run -d --rm --name myoauth -p 8080:8080 oauth-sample-service:latest
    $ docker run -d --rm --name myoauth \
        -p 443:8443 \
        -v /etc/letsencrypt:/etc/letsencrypt \
        -e APP_ENABLE_SSL=true \
        -e APP_SSL_CHAIN_FILE=/etc/letsencrypt/live/domain.com/fullchain.pem \
        -e APP_SSL_KEY_FILE=/etc/letsencrypt/live/domain.com/privkey.pem \
        oauth-sample-service:latest

Usage:

    $ curl http://localhost:8080/oauth-sample-service
    $ curl http://localhost:8080/oauth-sample-service/rest/health
