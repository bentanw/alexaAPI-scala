import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.model._
import akka.http.scaladsl.server._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import api.AlexaRoute

class AlexaRouteSpec extends AnyWordSpec with Matchers with ScalatestRouteTest {
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import AlexaRoute.{alexaRequest, AlexaPostRequest, AlexaResponse}

  val route: Route = alexaRequest

  "The Alexa API" should {
    "return 3 playables upon 'Alexa, play Howard Stern on SiriusXM'" in {
      Post("/api/alexa", AlexaPostRequest("Alexa, play Howard Stern on SiriusXM")) ~> route ~> check {
        status shouldBe StatusCodes.OK
        val response = responseAs[AlexaResponse]
        response.playable should have size 3
        response.playable.map(_.apply("title")) should contain allOf (
          "Howard Stern 24/7",
          "Howard Stern - Interview with Dave Grohl",
          "Howard Stern - Metallica, Miley Cyrus, and Elton John"
        )
      }
    }
  }

  "The Alexa API" should {
    "should return 1 playables upon Alexa, play Howard Stern 24/7 on SiriusXM'" in {
      Post("/api/alexa", AlexaPostRequest("Alexa, play Howard Stern 24/7 on SiriusXM")) ~> route ~> check {
        status shouldBe StatusCodes.OK
        val response = responseAs[AlexaResponse]
        response.playable should have size 1
        response.playable.map(_.apply("title")) should contain (
          "Howard Stern 24/7"
        )
      }
    }
  }

  "The Alexa API" should {
    "should return 1 playables upon Alexa, play SiriusXM NFL Radio on SiriusXM'" in {
      Post("/api/alexa", AlexaPostRequest("Alexa, play SiriusXM NFL Radio on SiriusXM")) ~> route ~> check {
        status shouldBe StatusCodes.OK
        val response = responseAs[AlexaResponse]
        response.playable should have size 1
        response.playable.map(_.apply("title")) should contain (
          "SiriusXM NFL Radio"
        )
      }
    }
  }

  "The Alexa API" should {
    "should return 1 playables upon Alexa, play Sports on SiriusXM" in {
      Post("/api/alexa", AlexaPostRequest("Alexa, play Sports on SiriusXM")) ~> route ~> check {
        status shouldBe StatusCodes.OK
        val response = responseAs[AlexaResponse]
        response.playable should have size 1
        response.playable.map(_.apply("title")) should contain (
          "SiriusXM NFL Radio"
        )
      }
    }
  }

  "The Alexa API" should {
    "should return 1 playables upon Alexa, play Elton John on SiriusXM" in {
      Post("/api/alexa", AlexaPostRequest("Alexa, play Elton John on SiriusXM")) ~> route ~> check {
        status shouldBe StatusCodes.OK
        val response = responseAs[AlexaResponse]
        response.playable should have size 1
        response.playable.map(_.apply("title")) should contain (
          "Howard Stern - Metallica, Miley Cyrus, and Elton John"
        )
      }
    }
  }
}