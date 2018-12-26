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
     * @param requestString the HTTP Request head
     * @return URL
     * @throws MalformedURLException
     */
    public static String parseURL(String requestString) throws MalformedURLException{
        String firstLine = null;
        for(int i = 0; i < requestString.length(); i ++){
            if(requestString.charAt(i) == '\r'){
                firstLine = requestString.substring(0, i);
                break;
            }
        }
        if(firstLine == null){
            System.out.println("MalformedURLException, URL:" + requestString);
            throw new MalformedURLException();
        }
        String url = firstLine.split(" ")[1];
        return url;
    }
}
