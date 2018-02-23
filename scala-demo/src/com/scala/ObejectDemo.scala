package com.scala

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

class ObejectDemo(val name:String,var age:Int) {
    //主函数
    println("执行主函数")

    try {
      val string: String = Source.fromFile("E:\\maopao.java").mkString
      println(string)
    } catch {
      case e:Exception =>e.printStackTrace()
    }

}

object ObejectDemo{
  def main(args: Array[String]): Unit = {
    val o = new ObejectDemo("nnnn",11)
    println(o)
  }
}

class SingletonDemo{
}
object SingletonDemo{
  /*def main(args: Array[String]): Unit = {
    //单例模式
    val a = SingletonDemo
    println(a)
    val b = SingletonDemo
    println(b)
  }*/
  var count = 5
  val demoes = new ArrayBuffer[SingletonDemo]()
  while (count>0){
    demoes +=new SingletonDemo
    count -=1
  }
  def getInstence():SingletonDemo={
    demoes.remove(0)
  }
}

//测试apply方法
class TestApply{

}
object TestApply{
  def apply(): Unit ={
    println("空参apply方法")
  }
  def apply(name:String):String={
    name
  }
  def main(args: Array[String]): Unit = {
      val d =  TestApply()
      val c = TestApply("vvvvvv")
    println(c)
  }
}

object test extends App{
  /*private val demo: SingletonDemo = SingletonDemo.getInstence()
  println(demo)*/
  val a = SingletonDemo
}

