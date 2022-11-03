package com.example1.actors

import com.example1.exceptions.Exceptions._
import com.example1.messages.NumberConverter._

import akka.actor.Actor

class NumberConverter extends Actor{
  override def receive: Receive = {
    // handling requests and sending a response
    case Conversion(number, conversionType) => try {
      sender ! SuccessfulConversion(commandParser(number.trim(), conversionType.trim().toLowerCase()))
    } catch {
      case _: NotValidRomanNumberException => sender ! Error("Number to convert to roman literals should be an integer greater than 0 and less than 4000")
      case _: NotDecimalNumberException => sender ! Error("Number to convert to hexadecimal should be an integer")
      case _: NumberFormatException => sender ! Error("Number to convert to hexadecimal should be between 0 and 2147483647")
      case _: UnknownCommandException => sender ! Error("Unknown command, please try again")
    }
    case _ => sender ! Error("Wrong request")
  }

  private def commandParser(number: String, converTo: String): String = {
    //parses commands in lower case without trailing spaces
    converTo match {
      case "rom" => toRoman(number)
      case "hex" => toHex(number)
      case _ => throw new UnknownCommandException
    }
  }
}
