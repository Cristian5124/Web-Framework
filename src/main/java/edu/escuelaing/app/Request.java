package edu.escuelaing.app;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an HTTP request with query parameter support.
 */

public class Request {
    private final String method;
    private final String path;
    @SuppressWarnings("unused")
    private final String queryString;
    private final Map<String, String> queryParams;

    /**
     * Constructor for Request.
     *
     * @param method      The HTTP method.
     * @param path        The request path.
     * @param queryString The query string.
     */

    public Request(String method, String path, String queryString) {
        this.method = method;
        this.path = path;
        this.queryString = queryString;
        this.queryParams = parseQueryParameters(queryString);
    }

    /**
     * Gets the value of a query parameter.
     *
     * @param name the parameter name
     * @return the parameter value
     */

    public String getValues(String name) {
        return queryParams.getOrDefault(name, "");
    }

    /**
     * Gets the HTTP method of the request.
     *
     * @return the HTTP method
     */

    public String getMethod() {
        return method;
    }

    /**
     * Gets the request path.
     *
     * @return the request path
     */

    public String getPath() {
        return path;
    }

    /**
     * Parses the query parameters from the query string.
     *
     * @param queryString the query string
     * @return a map of query parameter names to values
     */

    private Map<String, String> parseQueryParameters(String queryString) {
        Map<String, String> params = new HashMap<>();
        if (queryString == null || queryString.isEmpty()) {
            return params;
        }

        for (String pair : queryString.split("&")) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                params.put(key, value);
            }
        }
        return params;
    }
}
