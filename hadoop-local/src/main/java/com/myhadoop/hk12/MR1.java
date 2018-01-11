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
import com.myhadoop.hk12.LogBean;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MR1 {
    //0,
    static class MapperFirst extends Mapper<LongWritable, Text, Text, LogBean> {
        static SimpleDateFormat sd1 = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.US);
        static SimpleDateFormat sd2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        private Text k = new Text();
        private LogBean val = new LogBean();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            try {
                String line = value.toString();
                String[] fields = line.split(" ");

                if (fields.length>11) {
                    //ip
                    String ip = fields[0];
                    //time
                    String substring = fields[3].substring(1);
                    Date da = sd1.parse(substring);
                    String time = sd2.format(da);
                    //url
                    String url = fields[10];

                    val.set(time,ip,"",url,url);
                    k.set(ip);
                    context.write(k,val);
                }
                else {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    static class ReduceFirst extends Reducer<Text, LogBean, LogBean, NullWritable> {
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            Counter count = context.getCounter("logwash", "log");
            count.setValue(1);
        }

        @Override
        protected void reduce(Text key, Iterable<LogBean> values, Context context) throws IOException, InterruptedException {

            if (values!=null) {
                Counter counter = context.getCounter("logwash", "log");
                long num = counter.getValue();
                String from = "none";
                for (LogBean logbean :
                        values) {
                    logbean.setSession("s00" + num);
                    logbean.setReferal(from);
                    from = logbean.getUrl();

                    context.write(logbean,NullWritable.get());
                }
                counter.setValue(num+1);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(MR1.class);

        job.setMapperClass(MapperFirst.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LogBean.class);

        job.setReducerClass(ReduceFirst.class);
        job.setOutputKeyClass(LogBean.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.setInputPaths(job,new Path("F:/hadoop/hk12/data"));
        FileOutputFormat.setOutputPath(job,new Path("F:/hadoop/hk12/out1"));

        job.waitForCompletion(true);
        System.exit(0);
    }
}
