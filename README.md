# sample-oidc

Experiments with OIDC, featuring:

- Keycloak
- OIDC client in SPA
- OIDC client in Quarkus

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

See:

- https://www.keycloak.org/guides#server
- https://www.keycloak.org/getting-started/getting-started-docker
- https://www.keycloak.org/server/configuration
- https://www.keycloak.org/server/containers

~~~
$ docker run -d --name mykeycloak --rm \
  -p 8180:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:22.0.1 start-dev
~~~

## Realm Configuration

- Open http://localhost:8180/ and login as user "admin" with password "admin"
- Create a new realm "sample" and select it.
- Create a new user "philip":
  - Set username, email, first name, last name.
  - After creation, select the "Credentials" tab and set his password with "Temporary" off.
- Create a new user "guest":
  - Set username only.
  - After creation, select the "Credentials" tab and set his password with "Temporary" off.
- Create new groups "users", "admins" and "guests":
  - Dont set any additional properties.
  - Add users to groups using the "Members" tab of a group's details
    or using the "Groups" tab of a user's details.
- Create a new client "sample-spa" of type "OpenID Connect":
  - Set "Client authentication" off (public access).
  - Select the "Standard flow", "Direct access grants" and "Implicit flow".
  - Set "Root URL", "Home URL" and "Web origins" to "http://localhost:8080",
    and set "Valid redirect URIs" and "Valid post logout redirect URIs" to "http://localhost:8080/*"

## Realm Configuration (old)

- Import the realm configuration file to create a new realm
- Review settings in realm "Quarkus":
  - "SSO Session Idle" and "SSO Session Max"
  - "Client Session Idle" and "Client Session Max"
  - "Access Token Lifespan"
- When redirected from the application, login as user "alice" with password "alice"

## SPA

See:

- https://www.keycloak.org/docs/latest/securing_apps/index.html#_javascript_adapter

~~~
$ npm install keycloak-js
$ curl -O 'http://localhost:8080/js/keycloak.js'

$ docker run -d --name myspa --restart=always \
  -p 8080:80 \
  -v $(pwd):/usr/share/nginx/html:ro \
  nginx:alpine
~~~

TODO: call an endpoint to get data

TODO: refresh token, add error handling

TODO: parse the token, extract subject, username and roles (roles are not yet present)
