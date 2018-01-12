package com.myhadoop.entity;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SortDateBean implements WritableComparable<SortDateBean> {

    private String sort;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void write(DataOutput dataOutput) throws IOException {
            dataOutput.writeUTF(null==sort?"":this.sort);
    }

    public void readFields(DataInput dataInput) throws IOException {
            this.sort = dataInput.readUTF();
    }

    public int compareTo(SortDateBean o) {
        String vsort = o.getSort();
        String[] strarr1 = vsort.split(",");
        String[] strarr2 = this.sort.split(",");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long date1 =null;
        Long date2 = null;
        try {
            date1 = sdf.parse(strarr1[0]).getTime();
            date2 = sdf.parse(strarr2[0]).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1>date2 ? 0:1;

    }
}
