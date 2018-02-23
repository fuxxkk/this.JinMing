package com.scala

 class Scalademo private {
  val name :String= "lisi"
  var age :Int = 11
  private[this] var sex :String = "male"  //只能在当前类里面用,半生对象都不可以

  def printSex(): Unit ={
    println(this.sex)
  }
}

//伴生对象
object Scalademo{
  def main(args: Array[String]): Unit = {
    val scalademo = new Scalademo
    scalademo.age = 33
    println(scalademo.age)
    scalademo.printSex()


  }
}