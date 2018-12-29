package Controller;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * This class contains many HTTP related helper function
 */
public class HTTPLibrary {

    /**
     * Parse the pure url and paras from HTTP request url
     * @param url the pure url and paras String
     * @return
     */
    public static String[] parseURL(String url) {
        String[] res = new String[2];
        for(int i = 0; i < url.length(); i ++){
            if(url.charAt(i) == '?'){
                res[0] = url.substring(0, i);
                res[1] = url.substring(i + 1, url.length());
                break;
            }
        }
        if(res[0] == null) {
            res[0] = url;
        }
        return res;
    }

    /**
     * Parses key-value pairs for HTTP GET method from URL
     * @param para key-value string : (key=value&key=value)
     * @return JSON object contains all parameters
     */
    public static JSONObject parseParams(String para) {
        String[] paras = para.split("&");

        JSONObject result = new JSONObject();
        for (String tmp : paras) {
            String[] keyValue = tmp.split("=");
            result.put(keyValue[0], keyValue[1]);
        }
        return result;
    }

    /**
     * Parse the The first line from the HTTP Request
     * @param httpHead the HTTP Request head
     * @return String array for first element represents HTTP method
     * and second represents URL and third parameter represents HTTP protocol
     * @throws MalformedURLException
     */
    public static String[] parseHttpFirstLine(String httpHead) throws MalformedURLException{
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
        String[] paras = httpMessage.split("\r\n\r\n");
        return paras;
    }

    /**
     * Read the message from string, put it into an socketChannel
     * @param response The socket channel to write
     * @param msg The data byte array
     */
    public static void writeString(SocketChannel response, byte[] msg) throws IOException {
        //read from 0 and 1024 byte every time

        ByteBuffer block = ByteBuffer.wrap(msg);
        response.write(block);
        //response.close();

//        while(offset < msg.length) {
//            //put data into buffer
//            len = Math.min(msg.length - offset, block.capacity());
//            block.put(msg, offset, len);
//            offset += len;
//
//            //read data from buffer, write it to channel
//            block.flip();
//            response.write(block);
//            block.clear();
//        }
    }


    /**
     * In the case of client abnormal close connection, the read event
     * may always occurs. we need to close channel manually
     * @param key selector key associated to channel
     * @param sc socket channel
     * @throws IOException
     */
    public static void closeChannel(SelectionKey key, SocketChannel sc) throws IOException {
        sc.close();
        key.cancel();
        System.out.println("NIO BUG!");
    }
}


