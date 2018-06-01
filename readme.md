# oauth-sample-service

Build:

    $ mvn clean verify
    $ ./run.sh
    
Docker:

    $ mvn clean verify -P docker
    $ docker run -d --rm --name myoauth -p 8080:8080 oauth-sample-service:latest

Usage:

    - Browser: http://localhost:8080/oauth-sample-service
    $ curl http://localhost:8080/oauth-sample-service/rest/time
