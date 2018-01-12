package com.myhadoop.wash;

import com.myhadoop.entity.PageViewsBean;
import com.myhadoop.entity.VisitBean;
import com.myhadoop.entity.WebLogBean;
import com.sun.scenario.effect.impl.prism.ps.PPSBlend_MULTIPLYPeer;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Visit {

    static class VisitMapper extends Mapper<LongWritable, Text, Text, PageViewsBean> {
        Text k = new Text();
        PageViewsBean pageViewsBean = new PageViewsBean();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //(String session, String remote_addr, String timestr, String request, int step, String staylong, String referal, String useragent, String bytes_send, String status)
            //299d6b78-9571-4fa9-bcc2-f2567c46df3472.46.128.140-2013-09-18 07:58:50/hadoop-zookeeper-intro/160"https://www.google.com/""Mozilla/5.0"14722200

//            3fff2df4-249c-4d13-9f77-cdb6f99559ec1.80.249.223-2013-09-18 07:57:33/hadoop-hive-intro/1--none--"http://www.google.com.hk/url?sa=t&rct=j&q=hive%E7%9A%84%E5%AE%89%E8%A3%85&source=web&cd=2&ved=0CC4QFjAB&url=%68%74%74%70%3a%2f%2f%62%6c%6f%67%2e%66%65%6e%73%2e%6d%65%2f%68%61%64%6f%6f%70%2d%68%69%76%65%2d%69%6e%74%72%6f%2f&ei=5lw5Uo-2NpGZiQfCwoG4BA&usg=AFQjCNF8EFxPuCMrm7CvqVgzcBUzrJZStQ&bvm=bv.52164340,d.aGc&cad=rjt""Mozilla/5.0(WindowsNT5.2;rv:23.0)Gecko/20100101Firefox/23.0"14764200
//              a029d946-5e6f-45fe-bfed-8fb9d7343747116.24.236.1372013-09-18 08:50:35/hadoop-mahout-roadmap/3--none--"http://blog.fens.me/hadoop-family-roadmap/""Mozilla/5.0(WindowsNT6.1;WOW64)AppleWebKit/537.36(KHTML,likeGecko)Chrome/27.0.1453.116Safari/537.36"10335

            String[] fields = value.toString().split("\001");
            //System.out.println(fields[1]+"....."+fields[5]);
            Integer step = Integer.valueOf(fields[5]);
            pageViewsBean.set(fields[0],fields[1],fields[2],fields[3],fields[4],step,fields[6],fields[7],fields[8],fields[9]);
            k.set(fields[0]);
            context.write(k,pageViewsBean);
        }
    }

    static class VisitReduce extends Reducer<Text, PageViewsBean, VisitBean, NullWritable> {
        @Override
        protected void reduce(Text key, Iterable<PageViewsBean> values, Context context) throws IOException, InterruptedException {

            List<PageViewsBean> list = new ArrayList<PageViewsBean>();
            for (PageViewsBean pvBean :
                    values) {
                list.add(pvBean);
            }

            //按照步骤排序
            Collections.sort(list, new Comparator<PageViewsBean>() {
                public int compare(PageViewsBean o1, PageViewsBean o2) {

                    return o1.getStep()>o2.getStep()?-1:1;
                }
            });

            VisitBean visitBean = new VisitBean();
            PageViewsBean pvBeanfirst = list.get(0);
            PageViewsBean pvBeanlast = list.get(list.size() - 1);
            visitBean.setSession(pvBeanfirst.getSession());
            visitBean.setInPage(pvBeanfirst.getRequest());
            visitBean.setInTime(pvBeanfirst.getTimestr());
            visitBean.setOutPage(pvBeanlast.getRequest());
            visitBean.setOutTime(pvBeanlast.getTimestr());

            visitBean.setPageVisits(list.size());
            visitBean.setReferal(pvBeanfirst.getReferal());
            visitBean.setRemote_addr(pvBeanfirst.getRemote_addr());

            context.write(visitBean,NullWritable.get());
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(Visit.class);

        job.setMapperClass(VisitMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(PageViewsBean.class);

        job.setReducerClass(VisitReduce.class);
        job.setOutputKeyClass(VisitBean.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.setInputPaths(job,new Path("G:\\hadoop\\webdata\\pageviews"));
        FileOutputFormat.setOutputPath(job,new Path("G:\\hadoop\\webdata\\visit"));

        job.waitForCompletion(true);
        System.exit(0);
    }

}
