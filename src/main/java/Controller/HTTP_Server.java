package Controller;

import Model.DataBaseConnection;
import Model.DatabaseConnectionPool;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import redis.clients.jedis.Jedis;

import java.net.InetSocketAddress;
import java.util.List;


public class HTTP_Server {
    static boolean test = true;
    public static void main(String[] args){
        init();
        //ServerSocket serverSocket = null;
        NioEventLoopGroup group = new NioEventLoopGroup();
//        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .localAddress(new InetSocketAddress(80))
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            DataBaseConnection.storageIP(ch.remoteAddress());
                            ch.pipeline().addLast(new MyDecode());
                            ch.pipeline().addLast(new MyServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();
            future.channel().closeFuture().sync();
            group.shutdownGracefully().sync();


            //NIO method
//            Selector selector = Selector.open();
//            ServerSocketChannel ssc = ServerSocketChannel.open();
//
//            ssc.configureBlocking(false);
//            serverSocket = ssc.socket();
//            InetSocketAddress addr = new InetSocketAddress(80);
//            serverSocket.bind(addr);
//
//            ssc.register(selector, SelectionKey.OP_ACCEPT);
//            System.out.println("Begin Server....");
//
//            while (true) {
//                int num = selector.select();
//                //System.out.println(num);
//
//                Set<SelectionKey> selectKeys = selector.selectedKeys();
//                Iterator<SelectionKey> iter =selectKeys.iterator();
//                while(iter.hasNext()) {
//                    SelectionKey key = iter.next();
//                    if((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
//                        clientAccept(key, selector);
//                    } else if((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
//                        clientReadyToRead(key);
//                    }
//                    iter.remove();
//                }
//            }



        } catch (InterruptedException e) {
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
//    private static void clientAccept(SelectionKey key, Selector selector) throws IOException {
////        if(!test) {
////            return;
////        }
////        test = false;
//        System.out.println("accept!");
//        ServerSocketChannel newSSC = (ServerSocketChannel)key.channel();
//        SocketChannel sc = newSSC.accept();
//
//        sc.configureBlocking(false);
//        sc.register(selector, SelectionKey.OP_READ);
//
//    }
//
//    /**
//     * NIO method
//     * @param key registered key
//     * @throws IOException
//     */
//    private static void clientReadyToRead(SelectionKey key) throws IOException {
//        System.out.println(key.hashCode());
//        SocketChannel sc = (SocketChannel)key.channel();
//
//        Response response = new Response(sc, sc, key);
////        response.run();
////        new Thread(response).start();
//        ThreadPool.getInstance().executeTask(response);
//    }
}


class MyServerHandler extends ChannelInboundHandlerAdapter  {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);


        ByteBuf buf = (ByteBuf) msg;
        int len = buf.readableBytes();
        byte[] req = new byte[len];
        buf.readBytes(req);

//        System.out.println(new String(req));
//        System.out.println("done here");

        String requestMsg = new String(req);
        Response response = new Response(ctx, requestMsg);

        response.run();
//        ThreadPool.getInstance().executeTask(response);

    }




}

/**
 *
 */
class MyDecode extends ByteToMessageDecoder {
    private final String GET_DELIMITER = "\r\n\r\n";
    private final String POST_DELIMITER = "\r\n\r\n\r\n";

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object decoded = decode(ctx, in);
        if (decoded != null) {
            out.add(decoded);
        }
    }

    /**
     * Create a frame out of the {@link ByteBuf} and return it.
     *
     * @param   ctx             the {@link ChannelHandlerContext} which this {@link ByteToMessageDecoder} belongs to
     * @param   buffer          the {@link ByteBuf} from which to read data
     * @return  frame           the {@link ByteBuf} which represent the frame or {@code null} if no frame could
     *                          be created.
     */
    protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception{


        int len = buffer.readableBytes();
        if(len < 4){
            return null;
        }

        if(buffer.getByte(0) == 'G') {
            ByteBuf delimiter = Unpooled.copiedBuffer(GET_DELIMITER.getBytes());
            return decodeByDelimiter(buffer, delimiter);
        } else if(buffer.getByte(0) == 'P') {
            ByteBuf delimiter = Unpooled.copiedBuffer(POST_DELIMITER.getBytes());
            return decodeByDelimiter(buffer, delimiter);
        } else {
            return null;
        }
    }

    private Object decodeByDelimiter(ByteBuf buffer, ByteBuf delimiter) {
        ByteBuf frame = null;
        int frameLength = indexOf(buffer, delimiter);
        if(frameLength > 0) {
            frame = buffer.readRetainedSlice(frameLength);
            buffer.skipBytes(delimiter.capacity());
        }
        return frame;
    }

    /**
     * Returns the number of bytes between the readerIndex of the haystack and
     * the first needle found in the haystack.  -1 is returned if no needle is
     * found in the haystack.
     */
    private static int indexOf(ByteBuf haystack, ByteBuf needle) {
        for (int i = haystack.readerIndex(); i < haystack.writerIndex(); i ++) {
            int haystackIndex = i;
            int needleIndex;
            for (needleIndex = 0; needleIndex < needle.capacity(); needleIndex ++) {
                if (haystack.getByte(haystackIndex) != needle.getByte(needleIndex)) {
                    break;
                } else {
                    haystackIndex ++;
                    if (haystackIndex == haystack.writerIndex() &&
                            needleIndex != needle.capacity() - 1) {
                        return -1;
                    }
                }
            }

            if (needleIndex == needle.capacity()) {
                // Found the needle from the haystack!
                return i - haystack.readerIndex();
            }
        }
        return -1;
    }
}

