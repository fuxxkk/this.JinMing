package scala.com.myRPC

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
class Master extends Actor {

  println("Master Construter")

  override def preStart(): Unit = {
    println("preStart Master")
  }

  override def receive: Receive = {
    case "connect" =>{
      println("a worker connected")
      sender ! "reply"
    }
    case "hello" =>{
      println("hello")
    }
  }
}

object Master{
  def main(args: Array[String]): Unit = {
    val host = args(0)
    val port = args(1).toInt
    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
       """.stripMargin
    val config = ConfigFactory.parseString(configStr)
    //创建actorsystem,负责创建和监控以下actor,单例
    val actorSystem = ActorSystem("MasterSystem",config)
    val master = actorSystem.actorOf(Props(new Master),"master")
    master ! "hello"
    actorSystem.awaitTermination()
  }
}
