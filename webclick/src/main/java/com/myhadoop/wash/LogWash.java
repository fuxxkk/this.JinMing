package com.myhadoop.wash;

import com.myhadoop.entity.WebLogBean;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class LogWash {

    static class WashMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
        private Set<String> set = new HashSet<String>();
        private WebLogBean webLogBean = new WebLogBean();
        private Text k = new Text();
        private NullWritable v = NullWritable.get();
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            set.add("/about");
            set.add("/black-ip-list/");
            set.add("/cassandra-clustor/");
            set.add("/finance-rhive-repurchase/");
            set.add("/hadoop-family-roadmap/");
            set.add("/hadoop-hive-intro/");
            set.add("/hadoop-zookeeper-intro/");
            set.add("/hadoop-mahout-roadmap/");
            
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

             webLogBean = WebLogParserUtil.parserLog(value.toString());
             webLogBean = WebLogParserUtil.filtStaticResource(webLogBean, set);
            k.set(webLogBean.toString());
             context.write(k,v);

        }
    }


    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(LogWash.class);
        job.setMapperClass(WashMapper.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);


        FileInputFormat.setInputPaths(job,new Path("G:/hadoop/webdata/data"));
        FileOutputFormat.setOutputPath(job,new Path("G:/hadoop/webdata/out1"));
        job.setNumReduceTasks(0);
        job.waitForCompletion(true);
        System.exit(0);

    }
}
