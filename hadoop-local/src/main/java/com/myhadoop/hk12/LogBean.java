package com.myhadoop.hk12;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogBean implements WritableComparable<LogBean> {

    private String timeStamp;
    private String id;
    private String session;
    private String url;
    private String referal;

    public void set(String timeStamp, String id, String session, String url, String referal) {
        this.timeStamp = timeStamp;
        this.id = id;
        this.session = session;
        this.url = url;
        this.referal = referal;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReferal() {
        return referal;
    }

    public void setReferal(String referal) {
        this.referal = referal;
    }

    @Override
    public String toString() {
        return  timeStamp+","+id+","+session+","+url+","+referal;
    }

    public void write(DataOutput out) throws IOException {
        out.writeUTF(timeStamp);
        out.writeUTF(id);
        out.writeUTF(session);
        out.writeUTF(url);
        out.writeUTF(referal);
    }

    public void readFields(DataInput in) throws IOException {
        this.timeStamp = in.readUTF();
        this.id = in.readUTF();
        this.session = in.readUTF();
        this.url = in.readUTF();
        this.referal = in.readUTF();
    }

    public int compareTo(LogBean o) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long date1 =null;
        Long date2 = null;
        try {
             date1 = sdf.parse(o.getTimeStamp()).getTime();
             date2 = sdf.parse(this.timeStamp).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1>date2 ? 0:1;
    }
}
