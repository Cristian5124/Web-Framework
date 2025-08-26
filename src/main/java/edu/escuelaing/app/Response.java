package edu.escuelaing.app;

/**
 * Represents an HTTP response with status and content type.
 */

public class Response {
    private int statusCode = 200;
    private String contentType = "text/plain";

    /**
     * Sets the HTTP status code.
     * 
     * @param statusCode the status code
     */

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Gets the HTTP status code.
     * 
     * @return the status code
     */

    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the content type of the response.
     * 
     * @param contentType the content type
     */

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Gets the content type of the response.
     * 
     * @return the content type
     */

    public String getContentType() {
        return contentType;
    }
}
