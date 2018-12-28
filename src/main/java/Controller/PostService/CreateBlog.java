package Controller.PostService;

import java.io.IOException;
import java.io.OutputStream;

public class CreateBlog {
    public static void stroage(OutputStream response, String url, String body) throws IOException {
        String head = "HTTP/1.1 200 OK" + "\r\n" +
                "\r\n";
        response.write(head.getBytes());
        response.write(body.getBytes());
    }
}
