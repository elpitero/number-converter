package com.example1

import com.example1.actors.NumberConverter
import com.example1.messages.NumberConverter._

import scala.concurrent.duration.SECONDS
import scala.concurrent.{ExecutionContext, Future}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout

import scala.io.StdIn

/**
 * @author ${Piotr Dudziak}
 */


object ServiceMain{
  implicit val system: ActorSystem = ActorSystem("system")
  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val timeout: Timeout = Timeout(10, SECONDS)

  val num: ActorRef = system.actorOf(Props[NumberConverter])

  val route: Route =
    pathPrefix("numberconverter") {
      parameters("n", "c") { (number, convTo) =>
        get {
          val converted = num.ask(Conversion(number, convTo))
          onSuccess(converted) {
            case SuccessfulConversion(msg) => complete(200, msg)
            case Error(error) => complete(400, error)
          }
        }
      }
    }


  val bindingFuture: Future[Http.ServerBinding] = Http().newServerAt("localhost", 8080).bind(route)

  def main(args: Array[String]): Unit = {
    println(s"NumberConverter now online. Please navigate to http://localhost:8080/numberconverter\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}

