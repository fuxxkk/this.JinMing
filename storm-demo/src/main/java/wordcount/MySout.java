package wordcount;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MySout extends BaseRichSpout {

    SpoutOutputCollector spoutOutputCollector;
    Map<String, Values> buffer = new HashMap<String, Values>();

    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.spoutOutputCollector = spoutOutputCollector;
    }

    public void nextTuple() {
        String messageId = UUID.randomUUID().toString().replace("-","");
        Values value = new Values("i am lilei love hanmeimei");
        spoutOutputCollector.emit(value, messageId);
        buffer.put(messageId, value);
    }

    @Override
    public void ack(Object msgId) {
        System.out.println("消息处理成功===="+msgId);
        buffer.remove(msgId.toString());
    }

    @Override
    public void fail(Object msgId) {
        System.out.println("消息处理失败===="+msgId);
        Values vv = buffer.get(msgId.toString());
        spoutOutputCollector.emit(vv, msgId);
    }

    @Override
    public void ack(Object msgId) {

    }

    @Override
    public void fail(Object msgId) {

    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            outputFieldsDeclarer.declare(new Fields("love"));
    }
}
