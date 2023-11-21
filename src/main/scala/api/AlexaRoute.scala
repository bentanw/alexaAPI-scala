package api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat
import spray.json._

trait AlexaRouteDefinitions {
  def alexaRequest: Route
}

object AlexaRoute extends AlexaRouteDefinitions {
  val contentTable: List[Map[String, String]] = List(
    Map(
      "id" -> "1",
      "type" -> "channel",
      "group" -> "playable",
      "title" -> "Howard Stern 24/7"
    ),
    Map(
      "id" -> "2",
      "type" -> "episode",
      "group" -> "playable",
      "title" -> "Howard Stern - Interview with Dave Grohl"
    ),
    Map(
      "id" -> "3",
      "type" -> "episode",
      "group" -> "playable",
      "title" -> "Howard Stern - Metallica, Miley Cyrus, and Elton John"
    ),
    Map(
      "id" -> "4",
      "type" -> "channel",
      "group" -> "playable",
      "title" -> "SiriusXM NFL Radio"
    ),
    Map(
      "id" -> "5",
      "type" -> "show",
      "group" -> "container",
      "title" -> "Howard Stern"
    ),
    Map(
      "id" -> "6",
      "type" -> "category",
      "group" -> "container",
      "title" -> "Sports"
    )
  )
  val containerToPlayable: List[Map[String, String]] = List(
    Map("container_id" -> "5", "playable_id" -> "1"),
    Map("container_id" -> "5", "playable_id" -> "2"),
    Map("container_id" -> "5", "playable_id" -> "3"),
    Map("container_id" -> "6", "playable_id" -> "4")
  )

  
  // Define the structure of the expected POST request body and JSON response
  case class AlexaPostRequest(input: String)
  case class AlexaResponse(data: String)

  // Define a JSON format for the AlexaResponse case class
  implicit val alexaResponseFormat: RootJsonFormat[AlexaResponse] = jsonFormat1(AlexaResponse)
  implicit val alexaPostRequestFormat: RootJsonFormat[AlexaPostRequest] = jsonFormat1(AlexaPostRequest)

  // Route handling POST request
  def alexaRequest: Route = post {
    path("api" / "alexa") {
      entity(as[AlexaPostRequest]) { alexaPostRequest =>
        val response = AlexaResponse(alexaPostRequest.input)
        print(response)
        complete(response)
      }
    }
  }

  val allRoutes: Route = concat(alexaRequest)
}
