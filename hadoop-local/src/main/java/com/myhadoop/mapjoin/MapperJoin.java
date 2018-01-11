package com.myhadoop.mapjoin;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class MapperJoin {


    static class MapperJoinMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

        Map<String, String> infoMap = new HashMap<String, String>();
        Text text = new Text();
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {

            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("F:\\hadoop\\product.txt")));
            String line;
            while (StringUtils.isNotEmpty(line = bf.readLine())) {
                String[] fileds = line.split(",");
                infoMap.put(fileds[0],fileds[1]);
            }
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] fields = value.toString().split("\t");
            StringBuffer sb = new StringBuffer();
            String pdt = infoMap.get(fields[2]);

            text.set(sb.toString());
            context.write(text,NullWritable.get());

        }
    }


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setMapperClass(MapperJoinMapper.class);
        job.setOutputValueClass(NullWritable.class);
        job.setOutputKeyClass(Text.class);

        job.setNumReduceTasks(0);
        job.setJarByClass(MapperJoin.class);

        FileInputFormat.setInputPaths(job,new Path("F:\\hadoop\\mapinput"));
        FileOutputFormat.setOutputPath(job,new Path("F:\\hadoop\\mapoutput"));

        job.waitForCompletion(true);

    }
}
