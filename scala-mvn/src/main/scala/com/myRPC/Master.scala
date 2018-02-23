package scala.com.myRPC

import akka.actor.{Actor, ActorSystem, Props}
import com.myRPC._
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._
import scala.collection.mutable
class Master(val host:String,val port:Int) extends Actor {

  val workerMap = new mutable.HashMap[String,WorkerInfo]()
  val workers = new mutable.HashSet[WorkerInfo]()

  override def preStart(): Unit = {
    println(">>>>>>>>>>>>>preStart Master<<<<<<<<<<<<<<<<")
    //导入隐式转换
    import context.dispatcher
    context.system.scheduler.schedule(0 millis,15000 millis,self,CheckHeartBeat)
  }

  override def receive: Receive = {
    //注册
    case Register(id,memery,cores) =>{
      println("a worker register....")
      //如果id不存在就加入进内存
      if(!workerMap.contains(id)){
        println("it's a new register....")
        val workerinfo = new WorkerInfo(id, memery, cores)
        workerMap(id)=workerinfo
        workers  += workerinfo
        println("regist success!..")

        //发送注册成功消息
        sender ! Registered(s"akka.tcp://MasterSystem@$host:$port/user/master")
      }

    }
    case HeartBeatMsg(id) =>{
      println("recieve a worker heartbeat, workerId:"+id)
      if(workerMap.contains(id)){
        val workerInfo = workerMap(id)
        workerInfo.lastHeartBeatTiem = System.currentTimeMillis()
      }
    }
    case CheckHeartBeat =>{
      val filters = workers.filter(x=>System.currentTimeMillis()-x.lastHeartBeatTiem>15000)
      for(w <- filters) {
        workers -=w
        workerMap -=w.id
      }
      println(workerMap.size)
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
    val master = actorSystem.actorOf(Props(new Master(host,port)),"master")
    master ! "hello"
    actorSystem.awaitTermination()
  }
}
