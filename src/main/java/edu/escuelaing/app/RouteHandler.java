package edu.escuelaing.app;

/**
 * Functional interface for handling HTTP requests in REST routes.
 */

@FunctionalInterface
public interface RouteHandler {
    String handle(Request req, Response resp);
}
