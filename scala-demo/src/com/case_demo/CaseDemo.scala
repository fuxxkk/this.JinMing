package com.case_demo

import scala.util.Random

object CaseDemo extends App {
  /*
  //匹配类型
  val arr = Array("aaa","bbb","ccc")
  val str = arr(Random.nextInt(arr.length))
  println(str)
  str match {
    case "aaa" => println("a....")
    case "bbb" => println("b....")
    case _ => throw new Exception("not match...")
  }*/

  //匹配类型
  val arr = Array("aaa",-2.0,true,3,1.0)
  val elemet = arr(Random.nextInt(arr.length))
  println(elemet)
  elemet match {
    case x:Int => println("int类型"+x)
    case y:Double if(y>0) =>println("double:"+y)
    case z:Boolean =>println("boolean:"+z)
    case w:String => println("String:"+w)
    case _ => println("not match")
  }

  //匹配数组 元组 集合
  val arr2 = Array(0, 2, 3)
  arr2 match {
    case Array(1,x,y) => println(s"x:$x,y:$y")
    case Array(0) => println("only 0...")
    case Array(0,_*) =>println("0....")
  }

  val lst = List(1)
  lst match {
    case 1::Nil =>println("1::NIL")
    case 1::tail => println(lst)
    case x::y::z =>println("1,2,3")
    case _ => throw new Exception("not match")
  }
  val tup = (1,2,3,5)
  tup match {
    case (_,r,3,4) =>println("1,2,3,4")
    case (1,x,y,z) =>println(s"x:$x,y:$y,z:$z")

  }
}
