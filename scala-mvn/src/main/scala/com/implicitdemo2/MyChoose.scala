package com.implicitdemo2

import MyPredef.richoOrdingChoose

//视图限定,T<%Ordered[T] 是一个隐式转换,将T转成Ordered类型
class MyChoose[T<%Ordered[T]] {
  def choose(x:T,y:T):T={
    if(x>y) x else y
  }
}

//上下文限定,必须传进去一个隐式转换的值
class MyChoose2[T:Ordering]{
  def c (x:T,y:T) : T={
    //隐式转换的值
    val odr = implicitly[Ordering[T]]
    if(odr.gt(x,y)) x else y
  }
}

object MyChoose {

  def main(args: Array[String]): Unit = {

    val a = new Girl("aaa",90)
    val b = new Girl("bbb", 99)

    /*val choose = new MyChoose[Girl]
    val girl = choose.choose(a,b)
    println(girl.score)*/

    val girl = new MyChoose2[Girl]
    println(girl.c(a,b).score)
  }
}
