package com.constructor_demo

class demo {
  var name :String ="ccccc"
  def this(name:String){
    this()
    this.name=name
  }
}

object demo extends App {
  private val aaaa = new demo()
  println(aaaa.name)
}
