package com.myRPC

trait RemoteMessage extends Serializable

case class Register(id:String,memery:Int,cores:Int) extends RemoteMessage

case class Registered(msg:String)

case object HeartBeat

case class HeartBeatMsg(id:String)

case object CheckHeartBeat
