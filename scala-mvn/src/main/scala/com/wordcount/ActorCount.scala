package scala.com.wordcount

import com.sun.org.apache.xpath.internal.functions.FuncTrue

import scala.actors.{Actor, Future}
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

//map
class Task extends Actor{
  override def act(): Unit = {
      loop{
        react{
          case SubmitTask(filename:String) =>{
            val map = Source.fromFile(filename).getLines().flatMap(_.split(" ")).map((_,1)).toList.groupBy(_._1).mapValues(_.size)
            sender ! ResultTask(map)
          }
          case StopTask =>{
            exit()
          }
        }
      }
  }
}
case class SubmitTask(filename: String)
case class ResultTask(map: Map[String, Int])
object StopTask

object Run{
  def main(args: Array[String]): Unit = {
    val resultSet = new mutable.HashSet[Future[Any]]()
    val resultList = new ArrayBuffer[ResultTask]()
    val files = Array("E://hello.txt", "E://hello.log")
    for (f <- files) {
      val task = new Task
      val future = task.start() !! SubmitTask(f) //异步发送 ,非阻塞
      resultSet +=future
    }
    Thread.sleep(100)

    //reduce
    while (resultSet.size>0) {
      val toCompute = resultSet.filter(_.isSet)
      for (r <- toCompute) {
        val task = r.apply().asInstanceOf[ResultTask]
        resultList += task
        resultSet -= r
      }
    }

    val tuples = resultList.flatMap(_.map) //(hello,1),(tom,1)
    val tupleToInt = tuples.groupBy(_._1).mapValues(_.foldLeft(0)(_+_._2))
    println(tupleToInt)
  }
}

