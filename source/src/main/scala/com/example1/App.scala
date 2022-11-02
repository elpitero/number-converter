package com.example1

import akka.actor.{ActorSystem, Props}


/**
 * @author ${Piotr Dudziak}
 */
object App {
  def main(args : Array[String]): Unit = {
    val system = ActorSystem("System")
    val server = system.actorOf(Props[NumberConverter])
    val user = system.actorOf(Props(classOf[User], server))
    user ! Start
  }
}
