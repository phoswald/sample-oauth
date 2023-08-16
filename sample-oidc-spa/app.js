import './keycloak.js';

const keycloak = new Keycloak({
    url: 'http://localhost:8180/',
    realm: 'sample',
    clientId: 'sample-spa'
});

try {
    document.querySelector("#status").innerHTML = 'init() pending...'
    const authenticated = await keycloak.init({
        onLoad: 'login-required'
    });
    document.querySelector("#status").innerHTML = `init() successful: User is ${authenticated ? 'authenticated' : 'not authenticated'}`
} catch (error) {
    document.querySelector("#status").innerHTML = `init() failed: ${error.error}` 
    console.error('Failed to initialize adapter:', error);
}

document.querySelector("#show-token").addEventListener("click", function() { 
    document.querySelector("#token").innerHTML = keycloak.token
});
