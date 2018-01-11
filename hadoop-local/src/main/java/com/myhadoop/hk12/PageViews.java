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

public class PageViews {

    static class PageViewsMapper extends Mapper<LongWritable, Text, Text, PageViewsBean> {
        private Text k = new Text();
        private PageViewsBean v = new PageViewsBean();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] fields = value.toString().split(",");
            if(fields.length>=5&&!"\"-\"".equals(fields[3])){
                v.set(fields[2],fields[1],fields[0],fields[3],0L,0L);
                k.set(fields[1]+"-"+fields[2]);
                context.write(k,v);
            }

        }
    }

    static class PageViewsReduce extends Reducer<Text, PageViewsBean, PageViewsBean, NullWritable> {
        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @Override
        protected void reduce(Text key, Iterable<PageViewsBean> values, Context context) throws IOException, InterruptedException {
            Counter count = context.getCounter("pageview", "conuter");
            count.setValue(1);
            String stime = "none";
            try {
                for (PageViewsBean pb : values) {
                    long stepnum = count.getValue();
                    pb.setStep(stepnum);
                    if (!"none".equals(stime)) {
                        Date end = sdf.parse(pb.getTime());
                        Date begin = sdf.parse(stime);
                        long longtime = (end.getTime() - begin.getTime()) / (1000 * 60);
                        pb.setStayTime(longtime);
                    }
                    stime = pb.getTime();
                    count.setValue(stepnum+1);
                    context.write(pb,NullWritable.get());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(PageViews.class);

        job.setMapperClass(PageViewsMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(PageViewsBean.class);

        job.setReducerClass(PageViewsReduce.class);
        job.setOutputKeyClass(PageViewsBean.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.setInputPaths(job,new Path("F:/hadoop/hk12/out1/part-r-00000"));
        FileOutputFormat.setOutputPath(job,new Path("F:/hadoop/hk12/pageview1"));

        job.waitForCompletion(true);
        System.exit(0);
    }

}
