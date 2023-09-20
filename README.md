# sample-oidc

Experiments with OIDC, featuring:

- Keycloak
- OIDC client in SPA
- OIDC client in Quarkus

## URLs

- SPA: http://localhost:8080/
- Frontend: http://localhost:8081/
- Backend: http://localhost:8082/
- Keycloak: http://localhost:8180/

## Keycloak

See:

- https://www.keycloak.org/guides#server
- https://www.keycloak.org/getting-started/getting-started-docker
- https://www.keycloak.org/server/configuration
- https://www.keycloak.org/server/containers

~~~
$ docker run -d --name mykeycloak --restart=always \
  -p 8180:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:22.0.1 start-dev
~~~

### H2 Datenbank

Die H2-Datenbank wird unter `opt/keycloak/data/h2/` abgelegt.

~~~
$ docker cp mykeycloak:/opt/keycloak/data/h2 keycloak/h2/
~~~

### Themes

Links:

- https://www.baeldung.com/spring-keycloak-custom-themes
- https://www.baeldung.com/keycloak-custom-login-page

Die Themes sind in `/opt/keycloak/lib/lib/main/org.keycloak.keycloak-themes-*.jar` enthalten.
Es werden FreeMarker Template Files (`*.ftl`) verwendet.

Zusätzliche Themes können unter `/opt/keycloak/themes/` abgelegt werden.
Bei Verwendung von `start-dev` werden diese nicht gecached.

~~~
$ docker cp mykeycloak:/opt/keycloak/lib/lib/main/org.keycloak.keycloak-themes-22.0.1.jar keycloak/
~~~

### Realm Configuration

- Open http://localhost:8180/ and login as user "admin" with password "admin"
- Create a new realm "sample" and select it.
- Create a new user "philip":
  - Set username, email, first name, last name.
  - After creation, select the "Credentials" tab and set his password with "Temporary" to off.
- Create a new user "guest":
  - Set username only.
  - After creation, select the "Credentials" tab and set his password with "Temporary" to off.
- Create new groups "users", "admins" and "guests":
  - Dont set any additional properties.
  - Add users to groups using the "Members" tab of a group's details
    or using the "Groups" tab of a user's details.
- Create a new client "sample-spa" of type "OpenID Connect":
  - Set "Client authentication" to off (public access, i.a. no client secret).
  - Select the "Standard flow", "Direct access grants" and "Implicit flow".
  - Set "Root URL", "Home URL" and "Web origins" to "http://localhost:8080",
    and set "Valid redirect URIs" and "Valid post logout redirect URIs" to "http://localhost:8080/*"
- Create a new client "sample-frontend" of type "OpenID Connect":
  - Set "Client authentication" to on (non-public access, i.a. with client secret).
  - Select the "Standard flow", "Direct access grants" and "Service account roles".
  - Set "Root URL", "Home URL" and "Web origins" to "http://localhost:8081",
    and set "Valid redirect URIs" and "Valid post logout redirect URIs" to "http://localhost:8081/*"

## sample-oidc-spa

See:

- https://www.keycloak.org/docs/latest/securing_apps/index.html#_javascript_adapter

~~~
$ cd cd sample-oidc-spa/
$ npm install keycloak-js
$ curl -O 'http://localhost:8080/js/keycloak.js'

$ docker run -d --name myspa --rm \
  -p 8080:80 \
  -v $(pwd):/usr/share/nginx/html:ro \
  nginx:alpine
~~~

TODO: call an endpoint to get data

TODO: refresh token, add error handling

TODO: parse the token, extract subject, username and roles (roles are not yet present)

## sample-oidc-frontend

### Build and Run Standalone

~~~
$ mvn clean install
$ mvn quarkus:dev -f sample-oidc-frontend
~~~

~~~
$ mvn clean verify
$ cd sample-oidc-frontend
$ java -jar target/quarkus-app/quarkus-run.jar
~~~
