package com.implicitdemo2

object MyPredef {

  implicit def richChoose(g:Girl)= new Ordered[Girl]{
    override def compare(that: Girl): Int = {
      g.score-that.score
    }
  }

  //用ordering要用函数形式
  implicit val richoOrdingChoose= new Ordering[Girl]{
      override def compare(x: Girl, y: Girl): Int = {
        x.score-y.score
      }
    }

}
