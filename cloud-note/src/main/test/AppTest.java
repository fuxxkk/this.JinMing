import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class AppTest {

    private Jedis jedis;

    @Before
    public void init() {
        JedisPool jedisPool = new JedisPool("192.168.12.168", 6379);
        this.jedis = jedisPool.getResource();
    }

    @Test
    public void test() {
        String[] daxias = new String[]{"郭靖", "黄蓉", "令狐冲", "杨过", "林冲",
                "鲁智深", "小女龙", "虚竹", "独孤求败", "张三丰", "王重阳", "张无忌"
                , "王重阳", "东方不败", "逍遥子", "乔峰", "虚竹", "段誉"
                , "韦小宝", "王语嫣", "周芷若", "峨眉师太", "慕容复", "郭靖", "乔峰", "王重阳"};
        String[] daxiaArr = new String[]{"王语嫣", "周芷若", "峨眉师太", "慕容复","郭靖", "乔峰", "井中月"};

        jedis.sadd("aaa", daxias);
        jedis.sadd("bbb", daxiaArr);
        Set<String> sinter = jedis.sinter("aaa", "bbb");
        for (String s :
                sinter) {
            System.out.print(s+",");
        }
        System.out.println();
        jedis.sinterstore("aabb", "aaa", "bbb");
        jedis.expire("aabb", 10);
        Set<String> aabb = jedis.smembers("aabb");
        for (String s :
                aabb) {
            System.out.print(s+",");
        }
        System.out.println(jedis.scard("aabb"));
        jedis.close();
    }

}
