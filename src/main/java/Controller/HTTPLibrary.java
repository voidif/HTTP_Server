package Controller;

import org.json.JSONObject;

import java.net.MalformedURLException;

/**
 * This class contains many HTTP related helper function
 */
public class HTTPLibrary {
    /**
     * Parses key-value pairs for HTTP GET method from URL
     * @param url HTTP URL
     * @return JSON object contains all parameters
     */
    public static JSONObject getParams(String url) {
        String para = url.substring(6, url.length());
        String[] paras = para.split("&");

        JSONObject result = new JSONObject();
        for (String tmp : paras) {
            String[] keyValue = tmp.split("=");
            result.put(keyValue[0], keyValue[1]);
        }
        return result;
    }

    /**
     * Parse the url from the HTTP Request
     * @param httpHead the HTTP Request head
     * @return String array for first element represents HTTP method
     * and second represents URL
     * @throws MalformedURLException
     */
    public static String[] parseURL(String httpHead) throws MalformedURLException{
        String firstLine = null;
        for(int i = 0; i < httpHead.length(); i ++){
            if(httpHead.charAt(i) == '\r'){
                firstLine = httpHead.substring(0, i);
                break;
            }
        }
        if(firstLine == null){
            System.out.println("MalformedURLException, URL:" + httpHead);
            throw new MalformedURLException();
        }
        String[] paras = firstLine.split(" ");
        return paras;
    }

    /**
     * Get HTTP head from whole HTTP message
     * @param httpMessage incoming HTTP request message
     * @return An string array with size = 2 for HTTP head and body
     */
    public static String[] getHeadAndBody(String httpMessage) {
        String[] paras = httpMessage.split("/r/n/r/n");
        return paras;
    }
}
