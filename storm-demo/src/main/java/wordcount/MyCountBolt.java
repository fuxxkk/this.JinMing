package wordcount;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

public class MyCountBolt extends BaseRichBolt{
    OutputCollector outputCollector;
    Map<String, Integer> map = new HashMap<String, Integer>();
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.outputCollector = outputCollector;
    }

    public void execute(Tuple tuple) {
        String word = tuple.getString(0);
        Integer num = tuple.getInteger(1);

        if (map.containsKey(word)) {
            Integer n = map.get(word);
            map.put(word, n+num);
        }else {
            map.put(word, 1);
        }
        try {
            //Thread.sleep(1000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("count:"+map);
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
