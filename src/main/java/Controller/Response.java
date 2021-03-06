package Controller;

import Controller.GetService.JSON.JSONRequestHelper;
import Controller.GetService.StaticRequest;
import Controller.PostService.BlogStorage;
import io.netty.channel.ChannelHandlerContext;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;


public class Response implements Runnable{

    private SocketChannel request;
    private ChannelHandlerContext response;
    private String requestString;

    public Response(ChannelHandlerContext response, String requestString){
        this.requestString = requestString;
        this.response = response;
    }

    public void run(){
        responseMessage();
    }

    /**
     * Read request message from NIO channel
     * @return Request message string
     * @throws IOException
     */
    @Deprecated
    private String readRequest() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int len = request.read(buffer);
        //client abort error, close channel
        if(len <= 0) {
            System.out.println(len);
            //HTTPLibrary.closeChannel(key, request);
            return null;
        }

        StringBuilder res = new StringBuilder();
        while(len > 0) {
            res.append(new String(buffer.array(), 0, len));
            buffer.flip();
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
     * For POST, we support post the blog to server function
     */
    private void responseMessage(){

        try {

//            String message = readRequest();
//
//            //error message, return
//            if(message == null || message.length() == 0) {
//                return;
//            }

            //Get head and body
            String[] paras = HTTPLibrary.getHeadAndBody(requestString);
            String head = paras[0];
            String body = "";
            if(paras.length > 1) {
                body = paras[1];
            }
            //Get method and url and support protocol
            String[] httpFirstLine = HTTPLibrary.parseHttpFirstLine(head);
            String httpMethod = httpFirstLine[0];
            String url = httpFirstLine[1];

            byte[] reponseMsg = null;
            //Looking HTTP method
            //current support: GET, POST
            if(httpMethod.equals("GET")) {
                String[] getParas = HTTPLibrary.parseURL(url);
                if(url.length() >= 6 && url.substring(0, 6).equals("/json?")){
                    //invoke JSON related method
                    reponseMsg = JSONRequestHelper.invokeMethod(getParas[1]);
                } else {
                    //Static File Method
                    reponseMsg = StaticRequest.writeStaticFile(getParas[0]);
                }
            } else {
                //POST
                reponseMsg = BlogStorage.storage(url, body);
            }

            HTTPLibrary.writeResponse(response, reponseMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
