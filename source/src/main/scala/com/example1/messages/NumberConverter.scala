package com.example1.messages

import com.example1.exceptions.Exceptions._

import scala.collection.mutable
import scala.util.matching.Regex

object NumberConverter {

  sealed trait Response
  case class Error(error: String) extends Response
  case class SuccessfulConversion(response: String) extends Response

  sealed trait Request
  case class Conversion(number: String, conversionType: String) extends Request

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
      case None => throw new NotValidRomanNumberException
      case _ =>
        val rom = new mutable.StringBuilder()
        for (i <- 0 until num.length)
          rom.append(romanMapper(num(i), num.length - i - 1))
        rom.toString()
    }
  }

  def toHex(num: String): String = {
    //returns a number converted to hexadecimal
    decimalRegex findFirstIn num match {
      case None => throw new NotDecimalNumberException
      case _ => num.toInt.toHexString
    }
  }
}
