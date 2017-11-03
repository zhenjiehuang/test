package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

public class Test {
    public static void main(String[] args) {
        try {
            JedisShardInfo info = new JedisShardInfo("10.46.74.95", 9999);
            info.setPassword("6dbc0460f9a34cf9:Huimei2015");
            Jedis jedis = new Jedis(info);
            jedis.select(8);
            // HitExpress:47153:20171023:1
            // RecognitionResult:47599:20171023
            // for (byte[] key : jedis.keys("HitExpress*".getBytes())) {
            // System.out.println(new String(key));
            // jedis.del(key);
            // }

            jedis.del("HitExpress:23041:20171026:type");
            jedis.del("HitExpress:23041:20171026:1");
            jedis.del("HitExpress:23041:20171026:3");
            jedis.del("HitExpress:23041:20171026:4");
            jedis.del("RecognitionResult:23041:20171026");

            // System.out.println(jedis.lrange("HitExpress:23041:20171025:1", 0,
            // 100));
            // System.out.println("");
            // System.out.println(jedis.lrange("HitExpress:23041:20171025:3", 0,
            // 100));
            // System.out.println("");
            // System.out.println(jedis.hget("RecognitionResult:23041:20171025",
            // "29242"));

            jedis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
