import redis.clients.jedis.Jedis;

public class RedisTest {

    private static final String URL = "localhost";

    public static void main(String[] args) {
        Jedis jedis = new Jedis(URL);
        System.out.println("连接成功");
        //查看服务是否运行
        System.out.println("服务正在运行: "+jedis.ping());
    }
}
