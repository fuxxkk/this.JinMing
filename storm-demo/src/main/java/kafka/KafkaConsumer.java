package kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KafkaConsumer implements Runnable {

    KafkaStream<byte[], byte[]> stream;
    String group ;

    public KafkaConsumer(KafkaStream<byte[], byte[]> stream,String group) {
        this.group = group;
        this.stream = stream;
    }


    public void run() {
        ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
        while (iterator.hasNext()) {
            MessageAndMetadata<byte[], byte[]> data = iterator.next();
            String topic = data.topic();
            int partition = data.partition();
            long offset = data.offset();
            String msg = new String(data.message());
            System.out.println(String.format(
                    "Consumer: [%s],  Topic: [%s],  PartitionId: [%d], Offset: [%d], msg: [%s]",
                    group, topic, partition, offset, msg));
        }
        System.out.println(String.format("Consumer: [%s] exiting ...", group));
    }

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put("group.id", "dashujujiagoushi");
        props.put("zookeeper.connect", "mini1:2181,mini2:2181,mini3:2181,mini4:2181");
        props.put("auto.offset.reset", "largest");
        props.put("auto.commit.interval.ms", "1000");
        props.put("partition.assignment.strategy", "roundrobin");

        ConsumerConfig consumerConfig = new ConsumerConfig(props);
        ConsumerConnector con = Consumer.createJavaConsumerConnector(consumerConfig);
        String topic = "test";

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put(topic, 4);
        Map<String, List<KafkaStream<byte[], byte[]>>> streams = con.createMessageStreams(map);
        List<KafkaStream<byte[], byte[]>> kafkaStreams = streams.get(topic);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i =1;i<=4;i++){
            executorService.execute(new KafkaConsumer(kafkaStreams.get(i),"消费者_"+i));
        }
    }
}
