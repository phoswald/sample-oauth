# sample-oauth

## Build and Run Standalone

~~~
$ cd sample-oidc-frontend
$ mvn clean verify
$ java -jar target/quarkus-app/quarkus-run.jar
~~~

~~~
$ mvn quarkus:dev
~~~

## URLs

- http://localhost:8080/

## Keycloak

~~~
$ docker run -d --name mykeycloak --rm \
  -p 8180:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:17.0.0 start-dev
~~~

- Open http://localhost:8180/ and login as user "admin" with password "admin"
- Import the realm configuration file to create a new realm
- Review settings in realm "Quarkus":
  - "SSO Session Idle" and "SSO Session Max"
  - "Client Session Idle" and "Client Session Max"
  - "Access Token Lifespan"
- When redirected from the application, login as user "alice" with password "alice"
