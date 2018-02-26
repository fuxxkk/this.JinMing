package com.implicitdemo

object ImplicitValObj{

  //只能匹配一个,如有相同类型导入不能用"_"
  implicit val name ="123"
  implicit val name2 ="1234"
}

class ImplicitVal{
  def fun()(implicit name:String="lisi"): Unit ={
    println("hello "+name)
  }
}

object ImplicitDemo2 {
  def main(args: Array[String]): Unit = {
    val implicitVal = new ImplicitVal
    import ImplicitValObj.name
    implicitVal.fun()
  }
}
