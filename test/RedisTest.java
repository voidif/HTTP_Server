import redis.clients.jedis.Jedis;

public class RedisTest {

    private static final String URL = "localhost";

    public static void main(String[] args) {
        Jedis jedis = new Jedis(URL);
        System.out.println(jedis.get("test"));
    }
}
