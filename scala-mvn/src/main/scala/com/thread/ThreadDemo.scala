package scala.com.thread

import java.util.concurrent.{Callable, Executors, Future}

object ThreadDemo {
  def main(args: Array[String]): Unit = {
    val pool = Executors.newFixedThreadPool(10)
    val f : Future[Int]= pool.submit(new Callable[Int] {
      override def call(): Int = {
        Thread.sleep(10000)
        100
      }
    })
    var status = f.isDone
    println("pool status:"+status)
    Thread.sleep(15000)
    status = f.isDone
    println("pool status:"+status)
    println(f.get())
    pool.shutdown()
  }
}
