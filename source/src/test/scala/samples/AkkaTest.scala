package samples

import com.example1.messages.NumberConverter._
import com.example1.actors.NumberConverter

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

@RunWith(classOf[JUnitRunner])
class AkkaTest
  extends TestKit(ActorSystem("ActorTest"))
  with ImplicitSender
  with AnyWordSpecLike
  with Matchers
  with BeforeAndAfterAll{

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A NumberConverter actor" must {
    val numberConverter = system.actorOf(Props[NumberConverter])
    "Send back converted numbers" in {
      numberConverter ! Conversion("21", "rom")
      expectMsgAnyOf(SuccessfulConversion("XXI"))

      numberConverter ! Conversion("16", "hex")
      expectMsgAnyOf(SuccessfulConversion("10"))

      numberConverter ! Conversion("2000", "RoM")
      expectMsgAnyOf(SuccessfulConversion("MM"))

      numberConverter ! Conversion("2001314", "   Hex")
      expectMsgAnyOf(SuccessfulConversion("1e89a2"))
    }

    "Send back error messages" in {
      numberConverter ! Conversion("2.", "rom")
      expectMsgAnyOf(Error("Number to convert to roman literals should be an integer greater than 0 and less than 4000"))

      numberConverter ! Conversion("2.", "hex")
      expectMsgAnyOf(Error("Number to convert to hexadecimal should be an integer"))

      numberConverter ! Conversion("9999999999", "hex")
      expectMsgAnyOf(Error("Number to convert to hexadecimal should be between 0 and 2147483647"))

      numberConverter ! Conversion("", "")
      expectMsgAnyOf(Error("Unknown command, please try again"))

      numberConverter ! Conversion("222222", "..r")
      expectMsgAnyOf(Error("Unknown command, please try again"))

      numberConverter ! SuccessfulConversion("a")
      expectMsgAnyOf(Error("Wrong request"))

      numberConverter ! Error("")
      expectMsgAnyOf(Error("Wrong request"))

      numberConverter ! "a"
      expectMsgAnyOf(Error("Wrong request"))

      numberConverter ! (1,2)
      expectMsgAnyOf(Error("Wrong request"))
    }
  }
}
