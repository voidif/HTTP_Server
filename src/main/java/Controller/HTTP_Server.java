package Controller;

import Model.DataBaseConnection;
import Model.DatabaseConnectionPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTP_Server {
    public static void main(String[] args){
        init();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(80);
            System.out.println("Begin Server....");
            //always listen
            while(true){
                Socket socket = serverSocket.accept();
                //System.out.println("Client Connected! From : " + socket.getInetAddress().toString());
                //receive connection, create a new thread to handle it
                try{
                    Response response = new Response(socket.getInputStream(), socket.getOutputStream());
                    //System.out.println(socket.hashCode());
                    response.run();
                }
                catch (IOException e){
                    System.out.println("IO Error");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

    }
    public static void init(){
        //Initialize database connection pool
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();
        pool.init();
        //Initialize database connection
        DataBaseConnection.init();
    }
}
