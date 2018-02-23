package com.extends_demo

trait Animal {
  def run(): Unit ={
    println("Animal run")
  }
  def fly();
}

class People extends Animal{
  def fly(): Unit = {
    println("people can't fly")
  }

  /*override def run(): Unit = {
    println("people run")
  }*/
  val f1,f2,(a,b,c) ={
    println("abc")
    (1,2,3)
  }

  val (x,y,v)={
    (12,13,14)
  }
  val w :String = "aaa"
  println((x,y,v))
}


object People extends App {

    val p =  new  People
    p.fly()
    p.run()
}