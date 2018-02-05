package kafka2storm.project;

import com.alibaba.fastjson.JSON;
import kafka2storm.project.bean.OrderInfo;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.shade.org.apache.commons.collections.OrderedMap;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

public class KafkaToStorm {

    public static void main(String[] args) {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("kafkaSpout",
                new KafkaSpout(new SpoutConfig(new ZkHosts("mini1:2181,mini2:2181,mini3:2181,mini4:2181"),"test","/mykafka","kafkaSpout")), 1);
        topologyBuilder.setBolt("filterbolt", new FilterBolt(), 1).shuffleGrouping("kafkaSpout");

        Config config = new Config();
        config.setNumWorkers(1);

        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("kafka2storm",config,topologyBuilder.createTopology());

    }

    static class FilterBolt extends BaseRichBolt{

        private Jedis jedis;

        public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
            JedisPoolConfig config = new JedisPoolConfig();
            //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
            config.setMaxIdle(5);
            //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
            //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
            //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setMaxTotal(1000 * 100);
            //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
            config.setMaxWaitMillis(30);
            config.setTestOnBorrow(true);
            config.setTestOnReturn(true);
            /**
             *如果你遇到 java.net.SocketTimeoutException: Read timed out exception的异常信息
             *请尝试在构造JedisPool的时候设置自己的超时值. JedisPool默认的超时时间是2秒(单位毫秒)
             */
            JedisPool jedisPool = new JedisPool(config,"mini1", 6379);
            this.jedis = jedisPool.getResource();
        }

        public void execute(Tuple input) {
            String s = new java.lang.String((byte[]) input.getValue(0));
            System.out.println(s);
            if (s.contains("{\"createOrderTime\"")) {
                OrderInfo orderInfo = JSON.parseObject(s, OrderInfo.class);
                jedis.incrBy("totalCount", orderInfo.getProductPrice());
                System.out.println("存入redis");
            }
        }

        public void declareOutputFields(OutputFieldsDeclarer declarer) {

        }
    }

}



