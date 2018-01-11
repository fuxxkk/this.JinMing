package com.myhadoop.commonFriends;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Arrays;

public class CommonFriends2 {
    static class CFMapperTwo extends Mapper<LongWritable,Text,Text,Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] friend_persons = value.toString().split("\t");
            String[] persons = friend_persons[1].split(",");
            Arrays.sort(persons);

            //排序
            for(int i = 0;i<persons.length-2;i++) {
                for(int j =i+1;j<persons.length-1;j++) {
                    //p-p f
                    context.write(new Text(persons[i]+"-"+persons[j]),new Text(friend_persons[0]));
                }
            }

        }
    }

    static class CFReduceTwo extends Reducer<Text,Text,Text,Text> {

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            StringBuilder sb = new StringBuilder();
            for (Text t :
                    values) {
                sb.append(t + ",");
            }
            context.write(key,new Text(sb.toString()));
        }
    }

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(CommonFriends2.class);

        job.setMapperClass(CFMapperTwo.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(CFReduceTwo.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);


        FileInputFormat.setInputPaths(job,new Path("F:\\hadoop\\CFoutPut\\part-r-00000"));
        FileOutputFormat.setOutputPath(job,new Path("F:\\hadoop\\CFoutPut2"));

        job.waitForCompletion(true);
    }
}
