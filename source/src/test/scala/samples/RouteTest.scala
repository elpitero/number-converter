package samples
import akka.actor.Props
import akka.actor.typed.delivery.internal.ProducerControllerImpl.Request
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.testkit.TestKit
import com.example1.ServiceMain
import com.example1.ServiceMain.system
import com.example1.actors.NumberConverter
import org.junit.runner.RunWith
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner
import akka.http.scaladsl.model.StatusCodes._

@RunWith(classOf[JUnitRunner])
class RouteTest extends AnyWordSpec with Matchers with ScalatestRouteTest{
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "The service" must {
    "Return converted roman number as response for get request to /numberconverter/?n=1&c=rom" in {
      Get("/numberconverter/?n=1&c=rom") ~> ServiceMain.route ~> check {
        responseAs[String] shouldEqual "I"
      }
    }

    "Return converted hex number as response for get request to /numberconverter/?n=1&c=hex" in {
      Get("/numberconverter/?n=20&c=hex") ~> ServiceMain.route ~> check {
        responseAs[String] shouldEqual "14"
      }
    }


    "Return 400 Bad Request status code for get request with wrong parameters to /numberconverter/?n=1&c=he." in {
      Get("/numberconverter/?n=20&c=he.") ~> ServiceMain.route ~> check {
        status shouldEqual BadRequest
      }
    }

    "Return 400 Bad Request status code for get request with wrong parameters to /numberconverter/?n=1..1&c=hex" in {
      Get("/numberconverter/?n=1..1&c=hex") ~> ServiceMain.route ~> check {
        status shouldEqual BadRequest
      }
    }
  }
}
