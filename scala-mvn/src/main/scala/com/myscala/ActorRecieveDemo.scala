import scala.actors.Actor

class ActorRecieveDemo extends Actor {
  override def act(): Unit = {
    while (true) {
      receive{
        case "start" =>{
          val name = Thread.currentThread().getName
          println("线程:>>>>>>>"+name+"<<<<<<<<启动")
          Thread.sleep(5000)
        }
        case "stop" =>{
          val name = Thread.currentThread().getName
          println("线程:>>>>>>>"+name+"<<<<<<<<停止")
          Thread.sleep(5000)
        }
      }
    }
  }
}

class LoopDemo extends Actor{
  override def act(): Unit = {
    loop{
      react{
        case "start" =>{
          val name = Thread.currentThread().getName
          println("线程:>>>>>>>"+name+"<<<<<<<<启动")
          Thread.sleep(5000)
        }
        case "stop" =>{
          val name = Thread.currentThread().getName
          println("线程:>>>>>>>"+name+"<<<<<<<<停止")
          Thread.sleep(5000)
        }
        case "exit" =>{
          println("线程停止!")
          exit();
        }
      }
    }
  }
}

object Test extends App {
/*    private val demo = new ActorRecieveDemo
    demo.start()
    println("start..........")
    demo ! "start"
   println("stop..........")
    demo ! "stop"
  demo ! "stop"
  demo ! "stop"*/

  private val demo = new LoopDemo
  demo.start()
  demo ! "start"
  demo ! "stop"
  demo ! "stop"
  demo ! "exit"

}

