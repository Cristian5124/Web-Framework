package edu.escuelaing.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton router for managing routes.
 */

public class Router {
    private static Router instance;
    private final List<Route> routes;

    /**
     * Private constructor for the Router singleton.
     */

    private Router() {
        this.routes = new ArrayList<>();
    }

    /**
     * Gets the singleton instance of the Router.
     * 
     * @return the Router instance
     */

    public static Router getInstance() {
        if (instance == null) {
            instance = new Router();
        }
        return instance;
    }

    /**
     * Adds a new route to the router.
     * 
     * @param path    the route path
     * @param handler the route handler
     */

    public void addRoute(String path, RouteHandler handler) {
        routes.add(new Route(path, handler));
    }

    /**
     * Finds a route by its request path.
     * 
     * @param requestPath the request path
     * @return the matching Route, or null if not found
     */

    public Route findRoute(String requestPath) {
        for (Route route : routes) {
            if (route.matches(requestPath)) {
                return route;
            }
        }
        return null;
    }
}
