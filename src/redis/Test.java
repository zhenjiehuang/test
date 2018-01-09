package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

public class Test {
    public static void main(String[] args) {
        try {
            JedisShardInfo info = new JedisShardInfo("127.0.0.1", 6379);
            // info.setPassword("6dbc0460f9a34cf9:Huimei2015");
            Jedis jedis = new Jedis(info);
            // jedis.select(8);
            // HitExpress:47153:20171023:1
            // RecognitionResult:47599:20171023
            // for (byte[] key : jedis.keys("HitExpress*".getBytes())) {
            // System.out.println(new String(key));
            // jedis.del(key);
            // }

            // jedis.del("HitExpress:38723:20171109:type");
            // jedis.del("HitExpress:38723:20171109:1");
            // jedis.del("HitExpress:38723:20171109:2");
            // jedis.del("HitExpress:38723:20171109:3");
            // jedis.del("HitExpress:38723:20171109:4");
            // jedis.del("RecognitionResult:38723:20171026");

            System.out.println(jedis.lrange("HitExpress:156372:20171128:1", 0, -1));
            System.out.println(jedis.lrange("HitExpress:156372:20171128:3", 0, -1));
            System.out.println(jedis.lrange("HitExpress:156372:20171128:type", 0, -1));
            // System.out.println("");
            // System.out.println(jedis.lrange("HitExpress:23041:20171025:3", 0,
            // 100));
            // System.out.println("");
            System.out.println(jedis.hkeys("RecognitionResult:156372:20171128"));
            System.out.println(jedis.hget("RecognitionResult:156372:20171128", "179955"));

            jedis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
