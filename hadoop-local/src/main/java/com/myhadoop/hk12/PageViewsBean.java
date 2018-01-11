package com.myhadoop.hk12;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PageViewsBean implements Writable {

    private String session;
    private String userId;
    private String time;
    private String url;
    private Long stayTime;
    private Long step;


    public void set(String session, String userId, String time, String url, Long stayTime, Long step) {
        this.session = session;
        this.userId = userId;
        this.time = time;
        this.url = url;
        this.stayTime = stayTime;
        this.step = step;
    }


    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getStayTime() {
        return stayTime;
    }

    public void setStayTime(Long stayTime) {
        this.stayTime = stayTime;
    }

    public Long getStep() {
        return step;
    }

    public void setStep(Long step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return session+","+userId+","+time+","+url+","+stayTime+","+step;
    }

    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(session);
        dataOutput.writeUTF(userId);
        dataOutput.writeUTF(time);
        dataOutput.writeUTF(url);
        dataOutput.writeLong(stayTime);
        dataOutput.writeLong(step);
    }

    public void readFields(DataInput dataInput) throws IOException {
        this.session = dataInput.readUTF();
        this.userId = dataInput.readUTF();
        this.time = dataInput.readUTF();
        this.url = dataInput.readUTF();
        this.stayTime = dataInput.readLong();
        this.step = dataInput.readLong();
    }
}
