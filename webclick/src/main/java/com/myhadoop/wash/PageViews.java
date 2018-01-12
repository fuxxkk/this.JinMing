package com.myhadoop.wash;

import com.myhadoop.entity.WebLogBean;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class PageViews {

    static class pageViewsMapper extends Mapper<LongWritable,Text,Text,WebLogBean> {

        Text k = new Text();
        WebLogBean v = new WebLogBean();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {


        }
    }


    public static void main(String[] args) {

    }
}
