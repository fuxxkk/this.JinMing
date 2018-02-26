package com.implicitdemo

import java.io.File

import scala.io.Source

class ImplicitValue(val file:File){
  def read():String = {
    Source.fromFile(file.getPath).mkString
  }
}

object ImplicitValue {
  implicit def richFlieRead(file:File) {
    new ImplicitValue(file)}

}

object MainApp{
  def main(args: Array[String]): Unit = {
    val file = new File("d:/1.txt")
//    val content = file.read()
    //显式增强
    //val content = new ImplicitValue(file).read()

    //隐式增强
    import ImplicitValue.richFlieRead
   // val content = file.read()

   // println(content)
  }
}
