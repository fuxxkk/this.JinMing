package kafka2storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

public class Kafaka2Storm {

    public static void main(String[] args) {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("kafkaSpout",
                new KafkaSpout(new SpoutConfig(new ZkHosts("mini1:2181,mini2:2181"),"test","/myKafka","kafkaSpout")),1);
        topologyBuilder.setBolt("mybolt1", new Mybolt(), 1).shuffleGrouping("kafkaSpout");

        Config config = new Config();
        config.setNumWorkers(1);

        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("kafka2storm",config,topologyBuilder.createTopology());
    }


    static class Mybolt extends BaseRichBolt{

        public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {

        }

        public void execute(Tuple tuple) {
            String s = new String((byte[]) tuple.getValue(0));
            System.out.println(s);
        }

        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

        }
    }
}
