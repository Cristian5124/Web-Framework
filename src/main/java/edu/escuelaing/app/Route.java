package edu.escuelaing.app;

/**
 * Represents a route with path and handler.
 */

public class Route {
    private final String path;
    private final RouteHandler handler;

    /**
     * Constructor for Route.
     *
     * @param path    The route path.
     * @param handler The route handler.
     */

    public Route(String path, RouteHandler handler) {
        this.path = path;
        this.handler = handler;
    }

    /**
     * Gets the route path.
     * 
     * @return the route path
     */

    public String getPath() {
        return path;
    }

    /**
     * Gets the route handler.
     * 
     * @return the route handler
     */

    public RouteHandler getHandler() {
        return handler;
    }

    /**
     * Checks if the route matches the given request path.
     * 
     * @param requestPath the request path
     * @return true if the route matches, false otherwise
     */

    public boolean matches(String requestPath) {
        return path.equals(requestPath);
    }
}
