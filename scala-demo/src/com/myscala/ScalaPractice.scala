package com.myscala

/**
  * 课后练习
  */
object ScalaPractice {
  def main(args: Array[String]): Unit = {
  m2()
  }

  def m1 (): Unit ={
    //创建一个List
    val lst0 = List(1,7,9,8,0,3,5,4,6,2)
    //将lst0中每个元素乘以10后生成一个新的集合
    val lst1 = for(i<- lst0) yield i*10
    println("lst1:"+lst1)
    //将lst0中的偶数取出来生成一个新的集合
    val lst2 = for(i<- lst0 if i%2==0) yield i
    println("lst2:"+lst2)
    //将lst0排序后生成一个新的集合
    val lst3 = lst0.sorted
    println("lst3:"+lst3)
    //反转顺序
    val lst4 = lst3.reverse
    println("lst4:"+lst4)
    //将lst0中的元素4个一组,类型为Iterator[List[Int]]
    val ls5 = lst0.grouped(4).toIterator
    println("lst5":+ls5)
    //将Iterator转换成List
    val lst6 = ls5.toList
    println(lst6)
    //将多个list压扁成一个List
    val lst7 = lst6.flatten
    println(lst7)
  }

  def m2 (): Unit ={
    val lines = List("hello tom hello jerry", "hello jerry", "hello kitty")
    //先按空格切分，在压平
//    val lst1 = lines.map(_.split(" ")).flatten
    val lst1 = lines.flatMap(_.split(" "))
    println(lst1)
    //并行计算求和
    var lst2 = lst1.map((_,1))  //变成元组(hello,2)
   println(lst2)
    val lst3 = lst2.groupBy(_._1)
     println(lst3)
    val lst4 = lst3.mapValues(_.foldLeft(0)(_+_._2)).toList.sortBy(_._2).reverse.toMap
    println(lst4)
    //化简：reduce
    //将非特定顺序的二元操作应用到所有元素

    //安装特点的顺序

    //折叠：有初始值（无特定顺序）

    //折叠：有初始值（有特定顺序）

    //聚合
    val arr = List(List(1, 2, 3), List(3, 4, 5), List(2), List(0))

    val l1 = List(5,6,4,7)
    val l2 = List(1,2,3,4)
    //求并集
    val l3 = l1 ++ l2
    println(l3)
    //求交集
//    val l5 = l1.toSet & l2.toSet
    val l5 = l1.intersect(l2)
    println(l5)

    //求差集
    val l4 = l1.diff(l2)
    println(l4)
  }

}
