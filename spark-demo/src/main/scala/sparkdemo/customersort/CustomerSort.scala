package sparkdemo.customersort

import org.apache.spark.{SparkConf, SparkContext}

object RichGrilPredef{
  implicit val sortGirl = new Ordering[Girl2]{
    override def compare(x: Girl2, y: Girl2) = {
      if(x.value==y.value){
        y.age-x.age
      }else{
        x.value-y.value
      }
    }
  }
}


object CustomerSort {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("CustomSort").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(List(("yuihatano", 90, 28, 1), ("angelababy", 90, 27, 2),("JuJingYi", 95, 22, 3)))
    import RichGrilPredef._
    val rdd = rdd1.sortBy(x=>Girl2(x._2,x._3),false)

    println(rdd.collect().toBuffer)

    sc.stop()
  }
}

//第一种
case class Girl(value:Int,age:Int) extends Ordered[Girl] with Serializable{
  override def compare(that: Girl) = {
    if(value==that.value) {
      that.age-age
    }else{
      value-that.value
    }
  }
}


//第二种,隐式转换
case class Girl2(value:Int,age:Int) extends Serializable