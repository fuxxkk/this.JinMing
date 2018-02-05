package kafka.patition;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;
import org.apache.log4j.Logger;

public class MyPatition implements Partitioner {

    private static Logger logger = Logger.getLogger(MyPatition.class);

    public MyPatition(VerifiableProperties properties) {

    }
    //object o 为发送过来的messageId  ,i 为分区数
    public int partition(Object o, int i) {
        return Integer.valueOf(o.toString())%i;
    }
}

