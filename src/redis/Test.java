package redis;

import redis.clients.jedis.Jedis;

public class Test {
    public static void main(String[] args) {
        try {
            Jedis jedis = new Jedis("127.0.0.1", 6379);

            for (String key : jedis.keys("*")) {
                System.out.println(key);
            }

            jedis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
