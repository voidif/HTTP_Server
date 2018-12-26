package Controller;

import Controller.Service.JSON.JSONRequestHelper;
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
    private void Response(){

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

            //parseURL
            String url = HTTPLibrary.parseURL(head);

            if(url.length() >= 6 && url.substring(0, 6).equals("/json?")){
                //invoke JSON related method
                JSONRequestHelper.invokeMethod(response, url);
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
}
