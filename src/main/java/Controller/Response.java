package Controller;

import Controller.GetService.JSON.JSONRequestHelper;
import Controller.GetService.StaticRequest;
import Controller.PostService.CreateBlog;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;


public class Response implements Run{


    private SocketChannel request;
    private SocketChannel response;
    private SelectionKey key;

    public Response(SocketChannel sc, SocketChannel out, SelectionKey key){
        request = sc;
        response = sc;
        this.key = key;
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
        //client abort error, close channel
        if(len <= 0) {
            HTTPLibrary.closeChannel(key, request);
            return null;
        }

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

            //error message, return
            if(message == null || message.length() == 0) {
                return;
            }

            //Get head and body
            String[] paras = HTTPLibrary.getHeadAndBody(message);
            String head = paras[0];
            String body = "";
            if(paras.length > 1) {
                body = paras[1];
            }
            //Get method and url and support protocol
            String[] httpFirstLine = HTTPLibrary.parseHttpFirstLine(head);
            String httpMethod = httpFirstLine[0];
            String url = httpFirstLine[1];

            //Looking HTTP method
            //current support: GET, POST
            if(httpMethod.equals("GET")) {
                String[] getParas = HTTPLibrary.parseURL(url);
                if(url.length() >= 6 && url.substring(0, 6).equals("/json?")){
                    //invoke JSON related method
                    JSONRequestHelper.invokeMethod(response, getParas[1]);
                } else {
                    //Static File Method
                    StaticRequest.writeStaticFile(response, getParas[0]);
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
