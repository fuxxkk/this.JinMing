package com.implicitdemo2

object MyPredef2{

  implicit def choose(girl: Girl)={
    new Ordered[Girl] {
      override def compare(that: Girl) = {
        girl.score-that.score
      }
    }
  }

  implicit object select extends Ordering[Girl] {
    override def compare(x: Girl, y: Girl): Int = {
      x.score-y.score
    }
  }
}
