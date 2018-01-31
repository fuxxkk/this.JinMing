package wordcount;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

public class MyWordCount  {


    public static void main(String[] args) throws Exception {
        //准备topology
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        //设置spout 第一个参数:id名,第二个参数:spout类,第三个参数:并发数
        topologyBuilder.setSpout("spout1", new MySout(),1);
        //设置bolt,参数类型同上
        topologyBuilder.setBolt("bolt1", new MySplitBolt(), 2).shuffleGrouping("spout1");
        topologyBuilder.setBolt("bolt2", new MyCountBolt(), 4).fieldsGrouping("bolt1",new Fields("word"));

        //创建configuration,配置一个toplogy需要多少个worker
        Config config = new Config();
        config.setNumWorkers(2);

        //提交到本地.
        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("mywordcount",config,topologyBuilder.createTopology());
        Thread.sleep(60 * 60 * 1000);
        localCluster.shutdown();
    }

}
