# oauth-sample-service

Build:

    $ mvn clean verify
    $ ./run.sh
    
Docker:

    $ mvn clean verify -P docker
    $ docker run -d --rm --name my-oauth-sample -p 8080:8080 oauth-sample-service:latest

Usage:

    $ curl http://localhost:8080/
    $ curl http://localhost:8080/health
