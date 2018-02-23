package com.myscala

object HelloScala {
  def main(args: Array[String]): Unit = {
    println("hello scala")
   // println(1 to 10)

   /* for (i <- 1 to 3;j<- 1 to 3 if i!=j) {
      println("i:"+i+",j:"+j+",10*i+j:"+(10*i+j))
    }*/

    /* val arr = Array(1,2,3,4,5,6)
     var arr2 = for(i <- arr if (i%2==0)) yield i
     for (j<-arr2) {
       println(j)
     }*/
    //函数
   // val f1 = (x: Int, y: Int) => {(x + y)}
    val f2 = (x:Int,y:Int) =>{
      x-y
    }
    //定义函数 方式2
    val f3 :(Int,Double) =>String ={
      (a,b)=>a.+(b).toString
    }
    println(f3(1,3))
    /*println(f1(1,3))
    println(m1(1,2))
    println(m2(f2))
    println(m2(f1))*/
    val f4 = m1 _
    println(f4(2,3))
  }
  //定义方法
  def m1(x:Int,y:Int):Int ={
    x+y
  }

  //函数作为参数的方法
  def m2(f:(Int,Int) => Int):Int={
    f(2,6)
  }
}
