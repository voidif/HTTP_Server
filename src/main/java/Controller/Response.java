package Controller;

import Controller.GetService.JSON.JSONRequestHelper;
import Controller.GetService.StaticRequest;
import Controller.PostService.CreateBlog;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


public class Response implements Run{


    private SocketChannel request;
    private SocketChannel response;

    public Response(SocketChannel sc, SocketChannel out){
        request = sc;
        response = sc;
    }

    public void run(){
        Response();
    }

    /**
     * Read request message from NIO channel
     * @return Request message string
     * @throws IOException
     */
    private String readRequest() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int len = request.read(buffer);
        StringBuilder res = new StringBuilder();

        while(len > 0) {
            res.append(new String(buffer.array(), 0, len));
            len = request.read(buffer);
        }
        return res.toString();
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

            String message = readRequest();

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
                CreateBlog.storage(response, url, body);
            }
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
