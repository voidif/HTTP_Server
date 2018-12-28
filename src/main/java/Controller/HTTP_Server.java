package Controller;

import Model.DataBaseConnection;
import Model.DatabaseConnectionPool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class HTTP_Server {
    public static void main(String[] args){
        init();
        ServerSocket serverSocket = null;


        try {
//            serverSocket = new ServerSocket(80);
//            System.out.println("Begin Server....");
//            //always listen
//            while(true){
//                Socket socket = serverSocket.accept();
//                //System.out.println("Client Connected! From : " + socket.getInetAddress().toString());
//                //receive connection, create a new thread to handle it
//                try{
//                    Response response = new Response(socket.getInputStream(), socket.getOutputStream());
//                    //System.out.println(socket.hashCode());
//                    response.run();
//                }
//                catch (IOException e){
//                    System.out.println("IO Error");
//                    e.printStackTrace();
//                }
//            }

            //NIO method
            Selector selector = Selector.open();
            ServerSocketChannel ssc = ServerSocketChannel.open();

            ssc.configureBlocking(false);
            serverSocket = ssc.socket();
            InetSocketAddress addr = new InetSocketAddress(80);
            serverSocket.bind(addr);

            ssc.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Begin Server....");

            while (true) {
                int num = selector.select();
                //System.out.println(num);

                Set<SelectionKey> selectKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter =selectKeys.iterator();
                while(iter.hasNext()) {
                    SelectionKey key = iter.next();
                    if((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                        clientAccept(key, selector);
                    } else if((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                        clientReadyToRead(key);
                    }
                    iter.remove();
                }
            }



        } catch (IOException e) {
            e.printStackTrace();

        }

    }
    private static void init(){
        //Initialize database connection pool
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();
        pool.init();
        //Initialize database connection
        DataBaseConnection.init();
    }

    /**
     * NIO method
     */
    private static void clientAccept(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel newSSC = (ServerSocketChannel)key.channel();
        SocketChannel sc = newSSC.accept();

        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ);
    }

    /**
     * NIO method
     * @param key registered key
     * @throws IOException
     */
    private static void clientReadyToRead(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel)key.channel();
        Response response = new Response(sc, sc);
        response.run();
    }
}
