// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This is will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })

// IMPORTANT : Cypress client needs to be restarted to recognise new commands

Cypress.Commands.add("navigateToHome", () => {
  cy.visit("/");
});

Cypress.Commands.add("login", (appState = { targetUrl: "/" }) => {
  cy.log(`Logging in with Auh0`);
  const options = {
    method: "POST",
    url: Cypress.env("auth_url"),
    body: {
      grant_type: "password",
      username: Cypress.env("auth_username"),
      password: Cypress.env("auth_password"),
      audience: Cypress.env("auth_audience"),
      scope: "openid profile email",
      client_id: Cypress.env("auth_client_id"),
      client_secret: Cypress.env("auth_client_secret")
    }
  };

  cy.request(options).then(({ body }) => {
    const { access_token, expires_in, id_token } = body;

    cy.log(`Token from Auth0`, access_token);

    cy.server();

    // intercept Auth0 request for token and return what we have
    cy.route({
      url: "oauth/token",
      method: "POST",
      response: {
        access_token: access_token,
        id_token: id_token,
        scope: "openid profile email",
        expires_in: expires_in,
        token_type: "Bearer"
      }
    });

    // Auth0 SPA SDK will check for value in cookie to get appState
    // and validate nonce (which has been removed for simplicity)
    const stateId = "test";
    cy.setCookie(
      `a0.spajs.txs.${stateId}`,
      encodeURIComponent(
        JSON.stringify({
          appState: appState,
          scope: "openid profile email",
          audience: Cypress.env("auth_audience"),
          redirect_uri: "http://localhost:8080"
        })
      )
    ).then(() => {
      cy.visit(`/?code=test-code&state=${stateId}`);
    });
  });
});

Cypress.Commands.add(
    'iframeLoaded',
    {prevSubject: 'element'},
    ($iframe) => {
      const contentWindow = $iframe.prop('contentWindow');
      return new Promise(resolve => {
        if (
            contentWindow &&
            contentWindow.document.readyState === 'complete'
        ) {
          resolve(contentWindow)
        } else {
          $iframe.on('load', () => {
            resolve(contentWindow)
          })
        }
      })
    });


Cypress.Commands.add(
    'getInDocument',
    {prevSubject: 'document'},
    (document, selector) => Cypress.$(selector, document)
);

Cypress.Commands.add(
    'getWithinIframe',
    (targetElement) => cy.get('iframe').iframeLoaded().its('document').getInDocument(targetElement)
);