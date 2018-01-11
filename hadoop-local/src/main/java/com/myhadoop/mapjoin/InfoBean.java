package com.myhadoop.mapjoin;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 订单数据表t_order：
 * id	date	pid	amount
 1001	20150710	P0001	2
 1002	20150710	P0001	3
 1002	20150710	P0002	3

 商品信息表t_product
 id	pname	category_id	price
 P0001	小米5	1000	2
 P0002	锤子T1	1000	3

 */
public class InfoBean implements Writable {

    private Integer oid ;
    private Long data;
    private String pid;
    private String pname;
    private Integer categoryId;
    private Integer amount;
    private Double price;

    // flag=0表示这个对象是封装订单表记录
    // flag=1表示这个对象是封装产品信息记录
    private String flag;

    public InfoBean() {
    }

    public void set(Integer oid, Long data, String pid, Integer amount,String pname, Integer categoryId,  Double price, String flag) {
        this.oid = oid;
        this.data = data;
        this.pid = pid;
        this.pname = pname;
        this.categoryId = categoryId;
        this.amount = amount;
        this.price = price;
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "InfoBean{" +
                "oid=" + oid +
                ", data=" + data +
                ", pid='" + pid + '\'' +
                ", pname='" + pname + '\'' +
                ", categoryId=" + categoryId +
                ", amount=" + amount +
                ", price=" + price +
                '}';
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public Long getData() {
        return data;
    }

    public void setData(Long data) {
        this.data = data;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
    /*
     private Integer oid ;
    private Long data;
    private String pid;
    private String pname;
    private Integer categoryId;
    private Integer amount;
    private Double price;
     */
    public void write(DataOutput out) throws IOException {
        out.writeInt(oid);
        out.writeLong(data);
        out.writeUTF(pid);
        out.writeUTF(pname);
        out.writeInt(categoryId);
        out.writeInt(amount);
        out.writeDouble(price);
        out.writeUTF(flag);
    }

    public void readFields(DataInput in) throws IOException {

        this.oid = in.readInt();
        this.data = in.readLong();
        this.pid = in.readUTF();
        this.pname = in.readUTF();
        this.categoryId = in.readInt();
        this.amount = in.readInt();
        this.price = in.readDouble();
        this.flag = in.readUTF();
    }
}
