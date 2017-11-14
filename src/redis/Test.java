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

            // jedis.del("HitExpress:38723:20171109:type");
            // jedis.del("HitExpress:38723:20171109:1");
            // jedis.del("HitExpress:38723:20171109:2");
            // jedis.del("HitExpress:38723:20171109:3");
            // jedis.del("HitExpress:38723:20171109:4");
            // jedis.del("RecognitionResult:38723:20171026");

            System.out.println(jedis.lrange("HitExpress:38723:20171110:type", 0, 100));
            System.out.println(jedis.lrange("HitExpress:38723:20171110:3", 0, 100));
            System.out.println(jedis.lrange("HitExpress:38723:20171110:4", 0, 100));
            // System.out.println("");
            // System.out.println(jedis.lrange("HitExpress:23041:20171025:3", 0,
            // 100));
            // System.out.println("");
            System.out.println(jedis.hkeys("RecognitionResult:38723:20171110"));
            System.out.println(jedis.hget("RecognitionResult:38723:20171110", "44994"));
            System.out.println(jedis.hget("RecognitionResult:38723:20171110", "102164"));

            jedis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
