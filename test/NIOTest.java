import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOTest {
    /**
     * Java IO and NIO Test
     * @param args
     */
    public static void main(String[] args) throws Exception{
        FileInputStream fin = new FileInputStream("C:/Users/w5293/Desktop/result.txt");
        FileChannel fc = fin.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        fc.read(buffer);
        System.out.println(new String(buffer.array()));

    }
}
