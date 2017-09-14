package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

public class Test {
    public static void main(String[] args) {
        try {
            JedisShardInfo info = new JedisShardInfo("10.46.74.95", 9999);
            info.setPassword("6dbc0460f9a34cf9:Huimei2015");
            Jedis jedis = new Jedis(info);
            for (byte[] key : jedis.keys("*".getBytes())) {
                if (new String(key).contains("express")) {
                    System.out.println(new String(key));
                }
                // System.out.println(new String(key));
            }

            System.out.println(jedis.get("express_1"));

            jedis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
