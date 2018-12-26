package Controller;

import Controller.Service.StaticRequest;
import Model.DataBaseConnection;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;


public class GetResponse implements Run{


    private InputStream request;
    private OutputStream response;

    public GetResponse(InputStream in, OutputStream out){
        request = in;
        response = out;
    }

    public void run(){
        Response();
    }

    /**
     * Current support two type of request.
     * First, request for a static file in Server's storage. Dealing with
     * {@link StaticRequest}
     * Second, request for a JSON message. In this case, url always begin with
     * /json?.
     */
    public void Response(){

        try {
            byte[] buffer = new byte[2048];
            int i;
            try {
                i = request.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
                i = -1;
            }
            String head = new String(buffer);
//            System.out.println(head);

            //parseURL
            String url = parseURL(head);
            //System.out.println(url);
            //return index html or rate json
            if(url.length() >= 6 && url.substring(0, 6).equals("/json?")){
                //JSON Method
                //TODO
            } else {
                //Static File Method
                StaticRequest.writeStaticFile(response, url);
            }
            response.flush();
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Parse the url from the HTTP Request
     * @param requestString the HTTP Request head
     * @return URL
     * @throws MalformedURLException
     */
    public String parseURL(String requestString) throws MalformedURLException{
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


    /**
     * Return a json object to client.
     * @param response The response that the file will write in.
     * @param json The json to be returned.
     */
    public void writeJson(OutputStream response, JSONObject json) throws IOException{
        String head = "HTTP/1.1 200 OK" + "\r\n" +
                "Content-Type: application/json" + "\r\n" + "\r\n";
        response.write(head.getBytes());
        response.write(json.toString().getBytes());
    }
}
