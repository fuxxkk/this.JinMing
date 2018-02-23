package com.myRPC

import java.util.UUID

import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._

class Worker(val host:String,val port:Int,val memery:Int,val core:Int) extends Actor{
  var master:ActorSelection = _
  val id = UUID.randomUUID().toString


  override def preStart(): Unit = {
    master = context.actorSelection(s"akka.tcp://MasterSystem@$host:$port/user/master")
    master ! Register(id,memery,core)
  }

  override def receive: Receive = {
    //注册成功
    case Registered(msg) =>{
      println("recieve a master msg:"+msg)
      //发送心跳
      //导入隐式转换
      import context.dispatcher
      context.system.scheduler.schedule(0 millis,10000 millis,self,HeartBeat)
    }
    case HeartBeat =>{
      println("send master heartbeat")
      master ! HeartBeatMsg(id)
    }
  }
}


object Worker{
  def main(args: Array[String]): Unit = {
    val mastHost = args(0)
    val mastPort = args(1)
    val host = args(2)
    val port = args(3)
    val memery = args(4)
    val core = args(5)

    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
       """.stripMargin
    val config = ConfigFactory.parseString(configStr)
    val workerSystem = ActorSystem("WorkerSystem",config)
    val worker = workerSystem.actorOf(Props(new Worker(mastHost,mastPort.toInt,memery.toInt,core.toInt)),"worker")
    workerSystem.awaitTermination()
  }
}