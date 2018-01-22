package com.myhadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HbaseTest {
    private Configuration conf =null;
    private Table table = null;
    private Connection connection = null;

    @Before
    public void init() throws IOException {
         conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","192.168.12.61");
        conf.set("hbase.zookeeper.property.clientPort","2181");
        connection = ConnectionFactory.createConnection(conf);
        table = connection.getTable(TableName.valueOf("t_user"));
    }

    @Test
    public void test() {
        System.out.println(table);
    }

    @Test
    public void createTable() throws IOException {
        HBaseAdmin admin = new HBaseAdmin(conf);  //表管理类
        TableName tableName = TableName.valueOf("test2"); //表名
        HTableDescriptor tabledesc = new HTableDescriptor(tableName); //表的描述类
        HColumnDescriptor info1 = new HColumnDescriptor("info1");//列族类
        tabledesc.addFamily(info1);
        HColumnDescriptor info2 = new HColumnDescriptor("info2");
        tabledesc.addFamily(info2);

        admin.createTable(tabledesc);

    }

    @Test
    public void delete() throws Exception{
        HBaseAdmin admin = new HBaseAdmin(conf);
        admin.disableTable("test2");
        admin.deleteTable("test2");
        admin.close();
    }

    @Test
    public void put() throws IOException {
            Put put = new Put(Bytes.toBytes("000"));
            put.add(Bytes.toBytes("info1"),Bytes.toBytes("name"),Bytes.toBytes("lisi0"));
            put.add(Bytes.toBytes("info1"),Bytes.toBytes("age"),Bytes.toBytes(0));
            table.put(put);
    }

    @Test
    public void putList() throws IOException {
        List<Put> list = new ArrayList<Put>();
        Put put = null;
        for (int i=1;i<=10;i++) {
            put = new Put(Bytes.toBytes("lisi"+i));
            put.add(Bytes.toBytes("info"),Bytes.toBytes("name"),Bytes.toBytes("lisi"+i));
            put.add(Bytes.toBytes("info"),Bytes.toBytes("sex"),Bytes.toBytes(i%2));
            put.add(Bytes.toBytes("info"),Bytes.toBytes("age"),Bytes.toBytes(i));
            list.add(put);
        }
        table.put(list);
    }
    /*
    单挑查询
     */
    @Test
    public void get() throws Exception{
        Get get = new Get(Bytes.toBytes("list123_10"));
        Result result = table.get(get);
        byte[] name = result.getValue(Bytes.toBytes("info1"), Bytes.toBytes("name"));
        byte[] sex = result.getValue(Bytes.toBytes("info1"), Bytes.toBytes("sex"));
        byte[] age = result.getValue(Bytes.toBytes("info1"), Bytes.toBytes("age"));
        System.out.print(name!=null? Bytes.toString(name):"null");
        System.out.print(";");
        System.out.print(sex!=null?Bytes.toInt(sex)==0?"男":"女":"空");
        System.out.print(";");
        System.out.print(name!=null?Bytes.toInt(age):"空");
        System.out.println();
    }

    /**
     * s扫描全表
     * @throws Exception
     */
    @Test
    public void queryList() throws Exception {
        Scan scan = new Scan();
       /* scan.setStartRow(Bytes.toBytes("list123_1"));
        scan.setStopRow(Bytes.toBytes("list123_2"));*/
        //scan.addFamily(Bytes.toBytes("info1"));
        scan.addColumn(Bytes.toBytes("info"),Bytes.toBytes("name"));
        ResultScanner results = table.getScanner(scan);
        for (Result result :
                results) {
            byte[] rowkey = result.getRow();
            byte[] name = result.getValue(Bytes.toBytes("info"), Bytes.toBytes("name"));
            byte[] sex = result.getValue(Bytes.toBytes("info"), Bytes.toBytes("sex"));
            byte[] age = result.getValue(Bytes.toBytes("info"), Bytes.toBytes("age"));
            System.out.print(Bytes.toString(rowkey)+";");
            System.out.print(name!=null? Bytes.toString(name):"null");
            System.out.print(";");
            System.out.print(sex!=null?Bytes.toInt(sex)==0?"男":"女":"空");
            System.out.print(";");
            System.out.print(age!=null?Bytes.toInt(age):"空");
            System.out.println();
        }
    }
    /**
     * 全表扫描的过滤器
     * 列值过滤器
     * @throws Exception
     */
    @Test
    public void fileter() throws IOException {
        Scan scan = new Scan();
        //列族名,列名,比较表达式,值
        SingleColumnValueExcludeFilter filter = new SingleColumnValueExcludeFilter(Bytes.toBytes("info"), Bytes.toBytes("name"), CompareFilter.CompareOp.GREATER, Bytes.toBytes("lisi1"));
        scan.setFilter(filter);
        ResultScanner results = table.getScanner(scan);
        mymethod(results);

    }

    /**
     * rowkey过滤器
     * @throws Exception
     */
    @Test
    public void scanDataByFilter2() throws Exception {
        Scan scan = new Scan();
        //匹配规则,rowkey的正则
        RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL,new RegexStringComparator("^list*"));
        scan.setFilter(rowFilter);
        ResultScanner results = table.getScanner(scan);

        mymethod(results);
    }

    private void mymethod(ResultScanner results) {
        for (Result result :
                results) {
            byte[] rowkey = result.getRow();
            byte[] name = result.getValue(Bytes.toBytes("info"), Bytes.toBytes("name"));
            byte[] sex = result.getValue(Bytes.toBytes("info"), Bytes.toBytes("sex"));
            byte[] age = result.getValue(Bytes.toBytes("info"), Bytes.toBytes("age"));
            System.out.print(Bytes.toString(rowkey) + ";");
            System.out.print(name != null ? Bytes.toString(name) : "空");
            System.out.print(";");
            System.out.print(sex != null ? Bytes.toInt(sex) == 0 ? "男" : "女" : "空");
            System.out.print(";");
            System.out.print(age != null ? Bytes.toInt(age) : "空");
            System.out.println();
        }
    }

    /**
     * 匹配列名前缀
     * @throws Exception
     */
    @Test
    public void scanDataByFilter3() throws Exception {
        ColumnPrefixFilter filter = new ColumnPrefixFilter(Bytes.toBytes("name"));
        Scan scan = new Scan();
        scan.setFilter(filter);
        ResultScanner results = table.getScanner(scan);
        mymethod(results);
    }

    /**
     * 过滤器集合
     * @throws Exception
     */
    @Test
    public void scanDataByFilter4() throws Exception {
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        SingleColumnValueExcludeFilter singleColumnValueExcludeFilter = new SingleColumnValueExcludeFilter(Bytes.toBytes("info"), Bytes.toBytes("name"), CompareFilter.CompareOp.EQUAL, Bytes.toBytes("zhangshan1"));
        //ColumnPrefixFilter columnPrefixFilter = new ColumnPrefixFilter(Bytes.toBytes("na"));
        RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL,new RegexStringComparator("^zhangsan*"));
        filterList.addFilter(singleColumnValueExcludeFilter);
        filterList.addFilter(rowFilter);
        Scan scan = new Scan();
        scan.setFilter(filterList);
        ResultScanner results = table.getScanner(scan);
        mymethod(results);
    }
    @After
    public void after() throws Exception{
        table.close();
        connection.close();
    }
}
