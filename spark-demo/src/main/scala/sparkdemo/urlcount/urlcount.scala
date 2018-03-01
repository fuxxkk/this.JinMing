package sparkdemo.urlcount

import java.net.URL

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

object urlcount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("urldemo").setMaster("local[3]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.textFile("D:\\大数据第三期\\课件\\day29\\itcast.log").map(
       line=>{
        val f = line.split("\t")
         (f(1),1)
    })
    val rdd2 = rdd1.reduceByKey((x,y)=>x+y)

    val rdd3 = rdd2.map(x=>{
      val url = x._1
      val host = new URL(url).getHost
      (host,url,x._2)
    })

    val rdd4 = rdd3.groupBy(_._1).mapValues(it=>{
      it.toList.sortBy(_._3).take(3)
    })
    println(rdd4.collect().toBuffer)
    sc.stop()
  }
}

object URLCount2{
  def main(args: Array[String]): Unit = {
    val arr = Array("php.itcast.cn","java.itcast.cn","net.itcast.cn")
    val conf = new SparkConf().setAppName("urlcount").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.textFile("D:\\大数据第三期\\课件\\day29\\itcast.log").map(x=>{
      val f = x.split("\t")
      (f(1),1)
    })
      val rdd2 = rdd1.reduceByKey(_+_)
    val rdd3 = rdd2.map(x=>{
      val url = x._1
      val host = new URL(url).getHost()
      (host,url,x._2)
    })

    var tempArr = new ArrayBuffer[Any]()
    for(ins<-arr) {
      val rdd = rdd3.filter(_._1==ins)
      val temp = rdd.sortBy(_._3,true).take(3)
      println(temp.toBuffer)
      tempArr ++= temp
    }

    println(tempArr)
    sc.stop()
  }

}
object demo3{
  def main(args: Array[String]): Unit = {
    val arr = Array("php.itcast.cn","java.itcast.cn","net.itcast.cn")
    val conf = new SparkConf().setAppName("urlcount").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.textFile("D:\\大数据第三期\\课件\\day29\\itcast.log").map(line=>{
      val f = line.split("\t")
      (f(1),1)
    }).reduceByKey(_+_)
    val rdd2 = rdd1.map(x=>{
      val host = new URL(x._1).getHost
      (host,x._1,x._2)
    }).groupBy(_._1)
    val rdd3 = rdd2.mapValues(it=>{
      it.toList.sortBy(_._3).reverse.take(3)
    })
    println(rdd3.collect().toBuffer)
    rdd3.saveAsTextFile("f:/saprkOut/urlcount")
    sc.stop()
  }
}