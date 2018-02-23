package com.myRPC

import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

class Worker(val host:String,val port:Int) extends Actor{
  var master:ActorSelection = _

  override def preStart(): Unit = {
    master = context.actorSelection(s"akka.tcp://MasterSystem@$host:$port/user/master")
    master ! "connect"
  }

  override def receive: Receive = {
    case "reply" =>{
      println("recieve a master msg")
    }
  }
}


object Worker{
  def main(args: Array[String]): Unit = {
    val mastHost = args(0)
    val mastPort = args(1)
    val host = args(2)
    val port = args(3)

    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
       """.stripMargin
    val config = ConfigFactory.parseString(configStr)
    val workerSystem = ActorSystem("WorkerSystem",config)
    val worker = workerSystem.actorOf(Props(new Worker(mastHost,mastPort.toInt)),"worker")
    workerSystem.awaitTermination()
  }
}