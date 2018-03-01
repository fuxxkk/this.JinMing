package sparkdemo.areaCount

import org.apache.spark.{SparkConf, SparkContext}

object AreaCount {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("area").setMaster("local[4]")
    val sc = new SparkContext(conf)
    val rdd0 = sc.textFile("D:\\大数据第三期\\课件\\day29\\练习\\bs_log").map(line=>{
        val strarr = line.split(",")
       val phone = strarr(0)
      val flag = strarr(3)
      val time = if(flag=="1") -strarr(1).toLong else strarr(1).toLong
      val lac = strarr(2)
      ((lac,phone),time)
    })
    //ArrayBuffer(((CC0710CC94ECC657A8561DE549D940E0,18611132889),1900),
    // ((9F36407EAD0629FC166F14DDE7970F68,18611132889),54000),
    // ((CC0710CC94ECC657A8561DE549D940E0,18688888888),1300), ((16030401EAFB68F1E3CDF819735E1C66,18688888888),87600), ((16030401EAFB68F1E3CDF819735E1C66,18611132889),97500), ((9F36407EAD0629FC166F14DDE7970F68,18688888888),51200))
    val rdd1 = rdd0.reduceByKey(_+_,1)

    //准备join
    //ArrayBuffer((CC0710CC94ECC657A8561DE549D940E0,18611132889,1900), (9F36407EAD0629FC166F14DDE7970F68,18611132889,54000), (CC0710CC94ECC657A8561DE549D940E0,18688888888,1300), (16030401EAFB68F1E3CDF819735E1C66,18688888888,87600), (16030401EAFB68F1E3CDF819735E1C66,18611132889,97500), (9F36407EAD0629FC166F14DDE7970F68,18688888888,51200))
    val rdd2 = rdd1.map(x=>{
      val lac = x._1._1
      val phone = x._1._2
      val time = x._2
      (lac,(phone,time))
    })
    //ArrayBuffer((9F36407EAD0629FC166F14DDE7970F68,116.304864,40.050645), (CC0710CC94ECC657A8561DE549D940E0,116.303955,40.041935), (16030401EAFB68F1E3CDF819735E1C66,116.296302,40.032296))
    val rdd = sc.textFile("D:\\大数据第三期\\课件\\day29\\练习\\lac_info.txt").map(line=>{
      val arr = line.split(",")
      (arr(0),(arr(1),arr(2)))
    })
    //ArrayBuffer((CC0710CC94ECC657A8561DE549D940E0,((18611132889,1900),(116.303955,40.041935))), (CC0710CC94ECC657A8561DE549D940E0,((18688888888,1300),(116.303955,40.041935))), (16030401EAFB68F1E3CDF819735E1C66,((18688888888,87600),(116.296302,40.032296))), (16030401EAFB68F1E3CDF819735E1C66,((18611132889,97500),(116.296302,40.032296))), (9F36407EAD0629FC166F14DDE7970F68,((18611132889,54000),(116.304864,40.050645))), (9F36407EAD0629FC166F14DDE7970F68,((18688888888,51200),(116.304864,40.050645))))
    val rdd3 = rdd2.join(rdd)
    //ArrayBuffer((18611132889,CC0710CC94ECC657A8561DE549D940E0,1900,116.303955,40.041935), (18688888888,CC0710CC94ECC657A8561DE549D940E0,1300,116.303955,40.041935), (18688888888,16030401EAFB68F1E3CDF819735E1C66,87600,116.296302,40.032296), (18611132889,16030401EAFB68F1E3CDF819735E1C66,97500,116.296302,40.032296), (18611132889,9F36407EAD0629FC166F14DDE7970F68,54000,116.304864,40.050645), (18688888888,9F36407EAD0629FC166F14DDE7970F68,51200,116.304864,40.050645))
    val rdd4 = rdd3.map(x=>{
      val lac = x._1
      val phone = x._2._1._1
      val time = x._2._1._2
      val y = x._2._2._1
      val z = x._2._2._2
      (phone,lac,time,y,z)
    })
    val rdd5 = rdd4.groupBy(_._1)
    val rdd6 = rdd5.mapValues(it=>{
      it.toList.sortBy(_._3).reverse.take(2)
    })
    println(rdd6.collect().toBuffer)
    rdd6.saveAsTextFile("f:/sparkOut")
    sc.stop()
  }
}
