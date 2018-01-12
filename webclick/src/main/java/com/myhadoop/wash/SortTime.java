package com.myhadoop.wash;

import com.myhadoop.entity.SortDateBean;
import com.myhadoop.entity.WebLogBean;
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

public class SortTime {
    static class SortTimeMapper extends Mapper<LongWritable,Text,SortDateBean,WebLogBean> {
        WebLogBean v = new WebLogBean();
        SortDateBean k = new SortDateBean();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] fields = value.toString().split("\\001");
            if (fields.length<9) {
                return;
            }
           v.set("true".equals(fields[0])?true:false,fields[1],fields[2],fields[3],fields[4],fields[5],fields[6],fields[7],fields[8]);

            if (v.isValid()/*&&!"\"-\"".equals(fields[7])&&!"\"-\"".equals(fields[8])*/) {
                k.setSort(v.getTime_local()+","+v.getRemote_addr());
                context.write(k,v);
            }
        }
    }

    static class SortTimeReduce extends Reducer<SortDateBean, WebLogBean, WebLogBean, NullWritable> {
        NullWritable nullWritable = NullWritable.get();
        @Override
        protected void reduce(SortDateBean key, Iterable<WebLogBean> values, Context context) throws IOException, InterruptedException {

            for (WebLogBean webLogBean :values) {
                context.write(webLogBean,nullWritable);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(SortTime.class);

        job.setMapperClass(SortTimeMapper.class);
        job.setMapOutputKeyClass(SortDateBean.class);
        job.setMapOutputValueClass(WebLogBean.class);

        job.setReducerClass(SortTimeReduce.class);
        job.setOutputKeyClass(WebLogBean.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.setInputPaths(job,new Path("F:\\hadoop\\webclick\\out1"));
        FileOutputFormat.setOutputPath(job,new Path("F:\\hadoop\\webclick\\finnalout2"));

        job.waitForCompletion(true);
        System.exit(0);
    }
}
