package com.example1
import akka.actor.Actor

import scala.collection.mutable
import scala.language.postfixOps
import scala.util.matching.Regex

// definitions of messages
case class Msg(msg: String)
case class Res(response: String)
case class Error(error: String)
case object Stop
case object Start

object NumberConverter {
  val romanSymbols: Array[Char] = Array('I', 'X', 'C', 'M', 'V', 'L', 'D')
  val decimalRegex = new Regex("\\A(\\d+)\\Z")
  val romanRegex = new Regex("\\A(([1-3]\\d{3})|([1-9]\\d{0,2}))\\Z")

  def toRoman(num: String): String = {
    //returns a number converted to roman literals
    def romanMapper(c: Char, pos: Int): String = {
      /* inner function that maps a character in given position to its roman form. Position is the index of the character
       in string counted from right to left (starting from 0)*/
      val toReturn = new mutable.StringBuilder()
      for (i <-
             c match {
               case '1' => List(0)
               case '2' => List(0, 0)
               case '3' => List(0, 0, 0)
               case '4' => List(0, 4)
               case '5' => List(4)
               case '6' => List(4, 0)
               case '7' => List(4, 0, 0)
               case '8' => List(4, 0, 0, 0)
               case '9' => List(0, 1)
               case _ => Nil
             }) /* Lists are representations of digits using roman literals. Each number in list correspond to offset
                that should be added to position to get proper literal'*/
        toReturn.append(romanSymbols(pos + i))
      toReturn.toString()
    }

    // conversion
    romanRegex findFirstIn num match {
      case None =>  throw new NotValidRomanNumberException
      case _ =>
        val rom = new mutable.StringBuilder()
        for (i <- 0 until num.length)
          rom.append(romanMapper(num(i), num.length - i - 1))
        rom.toString()
    }
  }

  def toHex(num: String): String = {
    decimalRegex findFirstIn num match {
      case None => throw new NotDecimalNumberException
      case _ => num.toInt.toHexString
    }
  }
}

class NumberConverter extends Actor{
  override def receive: Receive = {
    // handling requests and sending a response
    case Stop => context.system.terminate
    case Msg(command) => try{
      sender ! Res(commandParser(command.trim().toLowerCase()))
    } catch {
      case _: NotValidRomanNumberException => sender ! Error("Number to convert to roman literals should be an integer greater than 0 and less than 4000")
      case _: NotDecimalNumberException => sender ! Error("Number to convert to hexadecimal should be an integer")
      case _: NumberFormatException => sender ! Error("Number to convert to hexadecimal should be between 0 and 2147483647")
      case _: UnknownCommandException => sender ! Error("Unknown command, please try again")
      case _: StopConnectionException => sender ! Stop
    }
    case _ => sender ! Error("Wrong request")
  }

  private def commandParser(command: String): String = {
    //parses commands in lower case without trailing spaces
    val commandRegex = new Regex("( ((rom)|(hex))\\Z)|(exit)")
    commandRegex findFirstIn command match {
      case Some(" rom") => NumberConverter.toRoman(command.substring(0, command.length - 4).trim())
      case Some(" hex") => NumberConverter.toHex(command.substring(0, command.length - 4).trim())
      case Some("exit") => throw new StopConnectionException
      case _ => throw new UnknownCommandException
    }
  }
}