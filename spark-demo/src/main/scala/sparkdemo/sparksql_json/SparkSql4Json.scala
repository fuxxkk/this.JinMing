package sparkdemo.sparksql_json

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkSql4Json {
  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME","root")
    val conf = new SparkConf().setAppName("sparksql4json").setMaster("local[2]")
    val spark = SparkSession.builder().config(conf).getOrCreate()

    val personRDD = spark.sparkContext.textFile("hdfs://mini1:9000/person.txt").map(line=>{
      val f = line.split(",")
      Person(f(0).toInt,f(1),f(2).toInt)
    })

    import spark.implicits._
    val personDF = personRDD.toDF()
    personDF.createOrReplaceTempView("person")

    val df = spark.sql("select * from person order by age desc")
    df.show
    df.write.json("hdfs://mini1:9000/json/")

    spark.stop()
  }
}

case class Person(id:Int,name:String,age:Int)

object SparkLoadJson{
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("sparksql4json").setMaster("local[2]")
    val spark = SparkSession.builder().config(conf).getOrCreate()

    val df = spark.read.json("hdfs://mini1:9000/json/")
   val personList =  df.rdd.map(line=>{
      List(Person(line.getAs[Long]("id").toInt,line.getAs[String]("name"),line.getAs[Long]("age").toInt))
    })
    println(personList.collect().toBuffer)
    spark.stop()
  }
}