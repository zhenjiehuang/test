package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

public class Test2 {

    public static void main(String[] args) {
        JedisShardInfo info = new JedisShardInfo("10.46.74.95", 9999);
        info.setPassword("6dbc0460f9a34cf9:Huimei2015");
        Jedis jedis = new Jedis(info);

        jedis.select(8);
        
        System.out.println(jedis.smembers("QC_CHIEF_SENIOR_DR_神内科"));

        // Set<String> sets = jedis.smembers("QCDiagnosisKeyCollection:86807");
        // System.out.println(sets);
        //
        // for (String set : sets) {
        // Map<String, String> str = jedis.hgetAll(set);
        // System.out.println(set + "\n" + str);
        // }
    }
}
