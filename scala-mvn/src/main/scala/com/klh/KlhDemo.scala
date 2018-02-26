package com.klh

object KlhDemo {

  def fun1(x:Int)(y:Int)={
    x*y
  }

  def main(args: Array[String]): Unit = {
    val func = fun1(3)(_)
    println(func(4))
  }

}
