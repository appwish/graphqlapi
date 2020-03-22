package io.appwish.graphqlapi.verticle.handler;

import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.auth.openidconnect.IdTokenVerifier;
import com.google.api.client.json.JsonFactory;
import io.appwish.graphqlapi.grpc.GrpcServiceStubsProvider;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

/**
 * This handler takes ID token from "Authorization" header and checks its validity. If the token is
 * valid, it injects user's data into request event context. If it's invalid, it does not inject
 * user's data.
 *
 * User's data can be then used in service business logic layer to authenticate/authorize users and
 * their actions.
 */
public class UserContextInjector implements Handler<RoutingContext> {

  private static final Logger LOG = LoggerFactory.getLogger(GrpcServiceStubsProvider.class);

  private final JsonFactory jsonFactory;
  private final IdTokenVerifier tokenVerifier;

  public UserContextInjector(final JsonFactory jsonFactory, final IdTokenVerifier tokenVerifier) {
    this.jsonFactory = jsonFactory;
    this.tokenVerifier = tokenVerifier;
  }

  @Override
  public void handle(final RoutingContext event) {
    try {
      final String authorizationHeaderValue = event.request().getHeader("authorization");
      final IdToken token = IdToken.parse(jsonFactory, authorizationHeaderValue);
      final boolean tokenValid = tokenVerifier.verify(token);

      if (tokenValid) {
        final String issuer = token.getPayload().getIssuer();
        final String email = (String) token.getPayload().get("email");
        final String givenName = (String) token.getPayload().get("given_name");
        final String familyName = (String) token.getPayload().get("family_name");

        final JsonObject userData = new JsonObject()
          .put("email", email)
          .put("firstName", givenName)
          .put("lastName", familyName)
          .put("identityProvider", issuer);

        event.put("user", userData);
      }
    } catch (final Exception e) {
      LOG.error("Could not check ID token validity", e);
    }
    event.next();
  }
}
