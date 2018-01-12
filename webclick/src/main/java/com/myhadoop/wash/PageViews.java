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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class PageViews {

    static class pageViewsMapper extends Mapper<LongWritable,Text,Text,WebLogBean> {

        Text k = new Text();
        WebLogBean v = new WebLogBean();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] fields = value.toString().split("\001");
            v.set("true".equals(fields[0])?true:false,fields[1],fields[2],fields[3],fields[4],fields[5],fields[6],fields[7],fields[8]);
            k.set(fields[1]);
            context.write(k,v);
        }
    }

    static class pageViewsReducer extends Reducer<Text, WebLogBean, Text, NullWritable> {
       /* Session string,
        remote_addr string,
        remote_user string,
        time_local string,
        request string,
        visit_step string,
        page_staylong string,
        http_referer string,
        http_user_agent string,
        body_bytes_sent string,
        status string*/

        Text k = new Text();
        NullWritable v = NullWritable.get();
        @Override
        protected void reduce(Text key, Iterable<WebLogBean> values, Context context) throws IOException, InterruptedException {

            List<WebLogBean> list = new ArrayList<WebLogBean>();
            for (WebLogBean web :
                    values) {
                list.add(web);
            }
            int step = 1;
            String session = UUID.randomUUID().toString();
            for(int i=0;i<list.size();i++) {
                WebLogBean web = list.get(i);

                //如果只有一条直接输出
                if (list.size()==1) {
                    k.set(session+"\001"+key.toString()+"\001"+web.getRemote_user()+"\001"+web.getTime_local()+"\001"+web.getRequest()+"\001"+step+"\001"+"--none--"+"\001"+web.getHttp_referer()+"\001"+
                            web.getHttp_user_agent()+"\001"+web.getBody_bytes_sent()+"\001"+web.getStatus());
                    context.write(k,v);
                    break;
                }

                //下一次再统计
                if (0==i) {
                    continue;
                }
                Long diff = timeDiff(list.get(i), list.get(i - 1));
                //30分钟内算一次会话
                if (diff < 1000 * 60 * 30) {
                    k.set(session + "\001" + key.toString() + "\001" + list.get(i - 1).getRemote_user() + "\001" + list.get(i - 1).getTime_local() + "\001" + list.get(i - 1).getRequest() +
                            "\001" + step + "\001" + diff + "\001" + list.get(i - 1).getHttp_referer() + "\001" + list.get(i - 1).getHttp_user_agent() + "\001" + list.get(i - 1).getBody_bytes_sent()+"\001" + list.get(i - 1).getStatus());
                    context.write(k, v);
                    step++;
                } else {
                    session = UUID.randomUUID().toString();
                    step=1;
                    k.set(session + "\001" + key.toString() + "\001" + list.get(i - 1).getRemote_user() + "\001" + list.get(i - 1).getTime_local() + "\001" + list.get(i - 1).getRequest() +
                            "\001" + step + "\001" + "--none--" + "\001" + list.get(i - 1).getHttp_referer() + "\001" + list.get(i - 1).getHttp_user_agent() +  "\001" +list.get(i - 1).getBody_bytes_sent()+"\001" + list.get(i - 1).getStatus());
                    step++;
                    context.write(k,v);
                }
                //最后一条直接输出
                if (i == list.size() - 1) {
                    k.set(session+"\001"+key.toString()+"\001"+web.getRemote_user()+"\001"+web.getTime_local()+"\001"+web.getRequest()+"\001"+step+"\001"+"--none--"+"\001"+web.getHttp_referer()+"\001"+
                            web.getHttp_user_agent()+"\001"+web.getBody_bytes_sent()+"\001"+web.getStatus());
                    context.write(k,v);
                }
            }

        }

        private Long timeDiff(WebLogBean webLogBean1,WebLogBean webLogBean2) {
            Long result = null;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long time1 = sdf.parse(webLogBean1.getTime_local()).getTime();
                long time2 = sdf.parse(webLogBean2.getTime_local()).getTime();
                result = time1-time2;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return result;
        }
    }


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(PageViews.class);

        job.setMapperClass(pageViewsMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(WebLogBean.class);

        job.setReducerClass(pageViewsReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.setInputPaths(job,new Path("G:\\hadoop\\webdata\\finalout"));
        FileOutputFormat.setOutputPath(job,new Path("G:\\hadoop\\webdata\\pageviews"));

        job.waitForCompletion(true);
        System.exit(0);
    }
}
