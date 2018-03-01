package sparkdemo.partitionDemo

import java.net.URL

import org.apache.spark.{Partitioner, SparkConf, SparkContext}

import scala.collection.mutable

object ParDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("urldemo").setMaster("local[3]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.textFile("E:\\大数据第三期\\课件\\day29\\itcast.log").map(line=>{
      val fields = line.split("\t")
      (fields(1),1)
    }).reduceByKey(_+_)

    val rdd2 = rdd1.map(x=>{
      val host = new URL(x._1).getHost()
      (host,(x._1,x._2))
    })

    val rdd3 = rdd2.map(_._1).distinct().collect()
    val partition = new HostPartition(rdd3)
    val rdd = rdd2.partitionBy(partition).mapPartitions(it=>{
      it.toList.sortBy(_._2).take(2).iterator
    })

    rdd.saveAsTextFile("F:/sparkOut/out3")

    println(rdd.collect().toBuffer)
    sc.stop()
  }
}

class HostPartition(rdd:Array[String]) extends Partitioner{

  val parMap = new mutable.HashMap[String,Int]()

  var count = 0
  for(str<-rdd) {
    parMap += (str -> count)
    count += 1
  }

  override def numPartitions = rdd.length

  override def getPartition(key: Any) = {
      parMap.getOrElse(key.toString,0)
  }
}
