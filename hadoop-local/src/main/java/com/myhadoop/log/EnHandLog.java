package com.myhadoop.log;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EnHandLog {

    static class LogMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
        Text k = new Text();
        NullWritable v = NullWritable.get();

        Map<String, String> map = new HashMap<String, String>();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            try {
                DBLoader.getINFO(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            Counter counter = context.getCounter("logCount", "log");
            String line = value.toString();
            String[] fields = line.split("\t");

            try {
                String url = fields[28];
                String val = map.get(url);
                if (val != null) {
                    k.set(line+"\t"+val+"\n");
                }else {
                    k.set(url + "\t" + "toCrwal" + "\n");
                }
                context.write(k,v);

            } catch (Exception e) {
                counter.increment(1);
            }

        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf);

        job.setJarByClass(EnHandLog.class);

        job.setMapperClass(LogMapper.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        // 要控制不同的内容写往不同的目标路径，可以采用自定义outputformat的方法
        job.setOutputFormatClass(EnhandLogWriter.class);

        FileInputFormat.setInputPaths(job, new Path("F:\\hadoop\\enhandlog\\data"));

        // 尽管我们用的是自定义outputformat，但是它是继承制fileoutputformat
        // 在fileoutputformat中，必须输出一个_success文件，所以在此还需要设置输出path
        FileOutputFormat.setOutputPath(job, new Path("F:\\hadoop\\enhandlog\\result"));

        // 不需要reducer
        job.setNumReduceTasks(0);

        job.waitForCompletion(true);
        System.exit(0);
    }

}
