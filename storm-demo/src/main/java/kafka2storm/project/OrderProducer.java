package kafka2storm.project;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka2storm.project.bean.OrderInfo;

import java.util.Properties;

public class OrderProducer {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("metadata.broker.list", "mini1:9092,mini2:9092,mini3:9092,mini4:9092");
        props.put("request.required.acks", "1");
        props.put("partitioner.class", "kafka.producer.DefaultPartitioner");

        Producer<String, String> producer = new Producer<String, String>(new ProducerConfig(props));

        for (int i =0;i<100000;i++) {
            String random = new OrderInfo().random();
            producer.send(new KeyedMessage<String,String>("test",i+"", random));
            System.out.println(random);
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
