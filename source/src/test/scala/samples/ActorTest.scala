package samples
import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.example1._
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

@RunWith(classOf[JUnitRunner])
class ActorTest
  extends TestKit(ActorSystem("ActorTest"))
  with ImplicitSender
  with AnyWordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A server actor" must {
    val server = system.actorOf(Props[NumberConverter])
    "Send back converted numbers" in {
      server ! Msg("21 rom")
      expectMsgAnyOf(Res("XXI"))

      server ! Msg("16 hex")
      expectMsgAnyOf(Res("10"))

      server ! Msg("2000 RoM")
      expectMsgAnyOf(Res("MM"))

      server ! Msg("2001314   Hex")
      expectMsgAnyOf(Res("1e89a2"))
    }

    "Send back error messages" in {
      server ! Msg("2. rom")
      expectMsgAnyOf(Error("Number to convert to roman literals should be an integer greater than 0 and less than 4000"))

      server ! Msg("2. hex")
      expectMsgAnyOf(Error("Number to convert to hexadecimal should be an integer"))

      server ! Msg("9999999999 hex")
      expectMsgAnyOf(Error("Number to convert to hexadecimal should be between 0 and 2147483647"))

      server ! Msg("")
      expectMsgAnyOf(Error("Unknown command, please try again"))

      server ! Msg("2rom")
      expectMsgAnyOf(Error("Unknown command, please try again"))

      server ! Msg("222222 ..r")
      expectMsgAnyOf(Error("Unknown command, please try again"))
    }

    "send back 'Stop' message" in {
      server ! Msg("Exit")
      expectMsgAnyOf(Stop)

      server ! Msg("EXit    ")
      expectMsgAnyOf(Stop)
    }
  }

}
