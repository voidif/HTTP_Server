import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTP_Server {
    public static void main(String[] args){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(80);
            System.out.println("Begin Server....");
            //always listen
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Client Connected! From : " + socket.getInetAddress().toString());
                //receive connection, create a new thread to handle it
                new Thread(){
                    @Override
                    public void run() {
                        try{
                            GetResponse response = new GetResponse(socket.getInputStream(), socket.getOutputStream());
                            response.Response();
                        }
                        catch (IOException e){
                            System.out.println("IO Error");
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

    }
    public void init(){

    }
}
