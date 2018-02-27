package sparkdemo.wordcount

import org.apache.spark.{SparkConf, SparkContext}

object wordcount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("wc")
    val sc = new SparkContext(conf)

    sc.textFile(args(0)).flatMap(_.split(" ")).map(x=>(x,1)).reduceByKey(_+_,1).sortBy(_._2).saveAsTextFile(args(1))
  }
}
