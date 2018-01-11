package com.myhadoop.log;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class EnhandLogWriter extends FileOutputFormat<Text,NullWritable> {

    /**
     * maptask或者reducetask在最终输出时，先调用OutputFormat的getRecordWriter方法拿到一个RecordWriter
     * 然后再调用RecordWriter的write(k,v)方法将数据写出
     */
    public RecordWriter<Text, NullWritable> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {

        //通过配置文件对象创建hdfs的文件系统
        Configuration conf = taskAttemptContext.getConfiguration();
        FileSystem fs = FileSystem.get(conf);
        FSDataOutputStream enOS = fs.create(new Path("F:\\hadoop\\enhandlog\\en\\data.log"));
        FSDataOutputStream crawlOS = fs.create(new Path("F:\\hadoop\\enhandlog\\crawl\\crawl.log"));

        return new LogWriter(enOS,crawlOS);
    }

    static class LogWriter extends RecordWriter<Text, NullWritable> {
        FSDataOutputStream enOS = null;
        FSDataOutputStream crawlOS = null;

        public LogWriter(FSDataOutputStream enOS,FSDataOutputStream crawlOS) {
            super();
            this.crawlOS = crawlOS;
            this.enOS = enOS;
        }

        public LogWriter() {
        }

        public void write(Text text, NullWritable nullWritable) throws IOException, InterruptedException {
            String line = text.toString();
            if (line.contains("toCrwal")) {
                crawlOS.write(line.getBytes("utf-8"));
            }else {
                enOS.write(line.getBytes("utf-8"));
            }
        }

        public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
            if (crawlOS != null) {
                crawlOS.close();
            }
            if (enOS != null) {
                enOS.close();
            }
        }
    }
}
