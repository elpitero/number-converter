package com.example1

import akka.actor.{Actor, ActorRef}

class User(server: ActorRef) extends Actor{
  override def receive: Receive = {
    case Start => listen()
    case Stop => context.system.terminate
    case Res(response) =>
      println(response)
      listen()
    case Error(error) =>
      println(error)
      listen()
    case other =>
      println(other)
      listen()
  }

  def listen(): Unit = {
    println("Enter number to convert and conversion type, or type 'EXIT' to terminate the program")
    val input = scala.io.StdIn.readLine().trim().toLowerCase()
    server ! Msg(input)
  }
}
