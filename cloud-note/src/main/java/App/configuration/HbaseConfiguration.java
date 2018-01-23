package App.configuration;

import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;

@Configuration
public class HbaseConfiguration {
    @Value("${hbase_zookeeper_quorum}")
    private String quorum;
    @Value("${hbase_zookeeper_property_clientPort}")
    private String port;
    @Value("${hbase_pool_size}")
    private Integer poolSize;

    @Bean(name = "hbaseConnection")
    @Primary
    public Connection getConnection() {
        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum",quorum);
        conf.set("hbase.zookeeper.property.clientPort",port);
        Connection connection =null;
        try {
            connection =  ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

}
