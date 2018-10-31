import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTP_Server {
    public static void main(String[] args){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(12121);
            System.out.println("Begin Server....");

            Socket socket = serverSocket.accept();
            System.out.println("Client Connected!");
            InputStream input = socket.getInputStream();
            byte[] buffer = new byte[2048];
            int i;
            try {
                i = input.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
                i = -1;
            }
            String head = new String(buffer);
            System.out.println(head);

            //response
            OutputStream out = socket.getOutputStream();
            String res = "HTTP/1.1 200 OK";
            out.write(res.getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
