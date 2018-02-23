package com.case_demo

import scala.util.Random

//样例类匹配
case class SubmitTask(id:String,name:String)
case class HeartBeats(time:Long)
case object CheckTimeOutTask

object CaseClassDemo extends App {
  val arr = Array(SubmitTask("0000","task-001"),CheckTimeOutTask,HeartBeats(1000L))
  arr(Random.nextInt(arr.length)) match {
    case SubmitTask(id,name) => println(s"id:$id,name:$name")
    case CheckTimeOutTask => println("time out")
    case HeartBeats(time) =>println(s"time:$time")
  }
}

//option匹配
object OptionDemo{
  def main(args: Array[String]): Unit = {
    val map = Map("a"->1,"b"->2)
    val a = map.get("c") match {
      case Some(i) => i
      case None => null
    }
    println(a)
    val c = map.getOrElse("c",null)
    println(c)
  }
}
//偏函数
object PartialFuncDemo{
  def func1:PartialFunction[String,Int]={
    case "one" => 1
    case _ =>{
      println("other")
      2
    }
  }
  def func2(x:String):Int = x match {
    case "one" =>1
    case _=>{
      println("other")
      0
    }
  }

  def main(args: Array[String]): Unit = {
    println(func1("11"))
    println(func2("one2"))
  }
}


