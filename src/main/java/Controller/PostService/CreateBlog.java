package Controller.PostService;

import Controller.HTTPLibrary;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class CreateBlog {
    public static void storage(SocketChannel response, String url, String body) throws IOException {
        String head = "HTTP/1.1 200 OK" + "\r\n" +
                "\r\n";

        String message = head.concat(body);
        HTTPLibrary.writeString(response, message.getBytes());
    }
}
