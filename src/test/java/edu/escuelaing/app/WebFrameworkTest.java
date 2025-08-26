package edu.escuelaing.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test suite for the Web Framework.
 */

public class WebFrameworkTest {

    @BeforeClass
    @SuppressWarnings("CallToPrintStackTrace")
    public static void startServer() throws Exception {
        
        new Thread(() -> {
            try {
                WebFramework.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(3000);
    }

    private String getResponse(String method, String urlStr) throws Exception {
        URI uri = new URI(urlStr);
        HttpURLConnection con = (HttpURLConnection) uri.toURL().openConnection();
        con.setRequestMethod(method);

        StringBuilder resp;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            resp = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                resp.append(line);
            }
        }
        return resp.toString();
    }

    private int getResponseCode(String method, String urlStr) throws Exception {
        URI uri = new URI(urlStr);
        HttpURLConnection con = (HttpURLConnection) uri.toURL().openConnection();
        con.setRequestMethod(method);
        return con.getResponseCode();
    }

    @Test
    public void testHelloEndpoint() throws Exception {
        String resp = getResponse("GET", "http://localhost:8080/hello?name=Cristian");
        assertTrue("Response should contain Hello Pedro", resp.contains("Hello Cristian"));
    }

    @Test
    public void testHelloEndpointWithoutName() throws Exception {
        String resp = getResponse("GET", "http://localhost:8080/hello");
        assertTrue("Response should contain Hello", resp.contains("Hello"));
    }

    @Test
    public void testPiEndpoint() throws Exception {
        String resp = getResponse("GET", "http://localhost:8080/pi");
        assertTrue("Response should contain PI value", resp.contains("3.14"));
    }

    @Test
    public void testTimeEndpoint() throws Exception {
        String resp = getResponse("GET", "http://localhost:8080/time");
        assertTrue("Response should contain time field", resp.contains("time"));
        assertTrue("Response should be JSON format", resp.contains("{") && resp.contains("}"));
    }

    @Test
    public void testGreetEndpoint() throws Exception {
        String resp = getResponse("GET", "http://localhost:8080/greet?name=Cristian&lang=es");
        assertTrue("Response should contain Spanish greeting", resp.contains("Hola Cristian"));
    }

    @Test
    public void testCalculatorEndpoint() throws Exception {
        String resp = getResponse("GET", "http://localhost:8080/calc?op=add&a=5&b=3");
        assertTrue("Response should contain result", resp.contains("result"));
        assertTrue("Response should contain the sum", resp.contains("8"));
    }

    @Test
    public void testStaticFileServing() throws Exception {
        int responseCode = getResponseCode("GET", "http://localhost:8080/index.html");
        assertEquals("Static file should return 200 OK", 200, responseCode);
    }
}
