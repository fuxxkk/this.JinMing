package com.myhadoop.hk12;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MR0 {
    static class MapperFirst0 extends Mapper<LongWritable, Text, LongWritable, Text> {
        static SimpleDateFormat sd1 = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.US);
        static SimpleDateFormat sd2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        private LongWritable k = new LongWritable();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            try {
                String line = value.toString();
                String[] fields = line.split(" ");

                if (fields.length>11) {
                    String url = fields[10];
                    if(!"\"-\"".equals(url)){
                        //time
                        String substring = fields[3].substring(1);
                        Date da = sd1.parse(substring);
                        String time = sd2.format(da);

                        k.set(da.getTime());
                        context.write(k,value);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    static class ReduceFirst0 extends Reducer<LongWritable, Text, Text, NullWritable> {

        @Override
        protected void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for (Text t :
                    values) {
                context.write(t, NullWritable.get());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(MR0.class);

        job.setMapperClass(MapperFirst0.class);
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(ReduceFirst0.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.setInputPaths(job,new Path("F:/hadoop/hk12/data"));
        FileOutputFormat.setOutputPath(job,new Path("F:/hadoop/hk12/out2"));

        job.waitForCompletion(true);
        System.exit(0);
    }
}
