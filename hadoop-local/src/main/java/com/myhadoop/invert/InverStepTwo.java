package com.myhadoop.invert;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class InverStepTwo {
    static Text k = new Text();
    static Text v = new Text();

    static class MapperStepTwo extends Mapper<LongWritable,Text,Text,Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] strs = value.toString().split(",");
            k.set(strs[0]);
            v.set(strs[1]);
            context.write(k,v);
        }
    }

    static class ReducerStepTwo extends Reducer<Text,Text,Text,Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            StringBuffer sb = new StringBuffer();
            for (Text t :
                    values) {
                sb.append(t.toString()+"\t");
            }
            k.set(key);
            v.set(sb.toString());
            context.write(k,v);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setMapperClass(MapperStepTwo.class);
        job.setMapOutputValueClass(Text.class);
        job.setMapOutputKeyClass(Text.class);

        job.setReducerClass(ReducerStepTwo.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job,new Path("F:\\hadoop\\invertoutput\\part-r-00000"));
        FileOutputFormat.setOutputPath(job,new Path("F:\\hadoop\\mapoutput2"));

        job.waitForCompletion(true);
    }
}
