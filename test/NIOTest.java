import javax.swing.text.html.HTMLDocument;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOTest {
    /**
     * Java IO and NIO Test
     * @param args
     */
    public static void main(String[] args) throws Exception{
//        FileInputStream fin = new FileInputStream("C:/Users/w5293/Desktop/result.txt");
//        FileChannel fc = fin.getChannel();
//
//        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        fc.read(buffer);
//        System.out.println(new String(buffer.array()));

        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();

        ssc.configureBlocking(false);
        ServerSocket socket = ssc.socket();
        InetSocketAddress addr = new InetSocketAddress(80);
        socket.bind(addr);

        ssc.register(selector, SelectionKey.OP_ACCEPT);


        while (true) {
            int num = selector.select();

            Set<SelectionKey> selectKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter =selectKeys.iterator();
            while(iter.hasNext()) {
                SelectionKey key = iter.next();
                if((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                    ServerSocketChannel newSSC = (ServerSocketChannel)key.channel();
                    SocketChannel sc = newSSC.accept();

                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);
                    System.out.println("find client!");
                } else if((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {

                    SocketChannel sc = (SocketChannel)key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(10);
                    int len = sc.read(buffer);
                    StringBuilder res = new StringBuilder();

                    res.append(new String(buffer.array(), 0, len));
                    len = sc.read(buffer);

                    System.out.print(res.toString());
                }
                iter.remove();
            }
        }

    }
}
