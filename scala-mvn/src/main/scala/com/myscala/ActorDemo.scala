package com.myscala

import scala.actors.Actor


object ActorDemo1 extends Actor{
  override def act(): Unit ={
    println(Thread.currentThread().getName)
    for(i<-0 to 10){
      println("a1-"+i)
    }
  }
}

object ActorDemo2 extends Actor{
  override def act(): Unit ={
    println(Thread.currentThread().getName)
    for(i<-0 to 10){
      println("a2-"+i)
    }
  }
}

object ActorDemo {
  def main(args: Array[String]): Unit = {
    ActorDemo2.start()
    ActorDemo2.start()
    println("main...")
  }
}
