package Controller;

import Controller.GetService.JSON.JSONRequestHelper;
import Controller.GetService.StaticRequest;
import Controller.PostService.CreateBlog;

import java.io.*;


public class Response implements Run{


    private InputStream request;
    private OutputStream response;

    public Response(InputStream in, OutputStream out){
        request = in;
        response = out;
    }

    public void run(){
        Response();
    }

    /**
     * Current support two type of request. GET and POST
     * For GET, we support following url request.
     * First, request for a static file in Server's storage. Dealing with
     * {@link StaticRequest}
     * Second, request for a JSON message. In this case, url always begin with
     * /json?.
     *
     * For POST, TODO
     */
    private void Response(){

        try {
            byte[] buffer = request.readAllBytes();
            String message = new String(buffer);

            //Get head and body
            String[] paras = HTTPLibrary.getHeadAndBody(message);
            String head = paras[0];
            String body = "";
            if(paras.length > 1) {
                body = paras[1];
            }
            //Get method and url
            String[] httpFirstLine = HTTPLibrary.parseURL(head);
            String httpMethod = httpFirstLine[0];
            String url = httpFirstLine[1];

            //Looking HTTP method
            //current support: GET, POST
            if(httpMethod.equals("GET")) {
                if(url.length() >= 6 && url.substring(0, 6).equals("/json?")){
                    //invoke JSON related method
                    JSONRequestHelper.invokeMethod(response, url);
                } else {
                    //Static File Method
                    StaticRequest.writeStaticFile(response, url);
                }
            } else {
                //POST
                CreateBlog.stroage(response, url, body);
            }
            response.flush();
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
