package edu.escuelaing.app;

import java.io.InputStream;

/**
 * Handles serving static files from a configurable directory.
 */

public class StaticFileHandler {
    private static StaticFileHandler instance;
    private String staticFilesPath = "/webroot";

    private StaticFileHandler() {
    }

    /**
     * Gets the singleton instance of the StaticFileHandler.
     * 
     * @return the StaticFileHandler instance
     */

    public static StaticFileHandler getInstance() {
        if (instance == null) {
            instance = new StaticFileHandler();
        }
        return instance;
    }

    /**
     * Sets the static files directory path.
     * 
     * @param path the static files directory path
     */

    public void setStaticFilesPath(String path) {
        this.staticFilesPath = path;
    }

    /**
     * Gets the static files directory path.
     * 
     * @return the static files directory path
     */

    public String getStaticFilesPath() {
        return staticFilesPath;
    }

    /**
     * Gets a static file from the configured directory.
     * 
     * @param requestPath the request path
     * @return the InputStream of the static file, or null if not found
     */

    public InputStream getStaticFile(String requestPath) {
        String fullPath = staticFilesPath + requestPath;
        return getClass().getResourceAsStream(fullPath);
    }

    /**
     * Checks if a static file exists.
     * 
     * @param requestPath the request path
     * @return true if the static file exists, false otherwise
     */

    public boolean staticFileExists(String requestPath) {
        return getStaticFile(requestPath) != null;
    }
}
