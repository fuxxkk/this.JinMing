package App.controller;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import javax.annotation.Resource;
import java.io.IOException;

@Controller
public class TestController {

    @Autowired
    private Jedis jedis;
    @Resource(name = "hbaseConnection")
    private Connection con;

    @RequestMapping("/test")
    public ModelAndView test() throws IOException {
        ModelAndView mv = new ModelAndView("/login/login");
        System.out.println(jedis);
        jedis.set("aaa", "bbb");

        System.out.println(con);
        Table table = con.getTable(TableName.valueOf("t_test"));
        Get get = new Get("111111".getBytes());
        Result result = table.get(get);
        byte[] value = result.getValue("c1".getBytes(), "cccccc".getBytes());
        System.out.println("value=="+ Bytes.toString(value));


        return mv;
    }
}
