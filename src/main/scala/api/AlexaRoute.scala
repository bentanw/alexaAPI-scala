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

  // Define structure of POST request body and JSON response, define JSON format
  case class AlexaPostRequest(input: String)
  case class AlexaResponse(data: List[Map[String, String]])
  implicit val alexaResponseFormat: RootJsonFormat[AlexaResponse] = jsonFormat1(
    AlexaResponse.apply
  )
  implicit val alexaPostRequestFormat: RootJsonFormat[AlexaPostRequest] =
    jsonFormat1(AlexaPostRequest.apply)

  // Route handling POST request
  def alexaRequest: Route = post {
    path("api" / "alexa") {
      entity(as[AlexaPostRequest]) { alexaPostRequest =>
        val requestData = alexaPostRequest.input
        val titlePattern = "play (.*) on".r
        val title = titlePattern
          .findFirstMatchIn(requestData)
          .map(_.group(1).trim)
          .getOrElse("")

        if (title.isEmpty) {
          complete(
            StatusCodes.BadRequest,
            "Please provide a valid input in the format of 'Hey Alexa, play ___ on SiriusXM'"
          )
        } else {
          val containers: List[Map[String, String]] =
            contentTable.filter(item =>
              item("title").contains(title) && item("group") == "container"
            )

          val playableIds = containerToPlayable.flatMap { ctp =>
            containers
              .find(_.apply("id") == ctp("container_id"))
              .map(_ => ctp("playable_id"))
          }

          val playables = if (containers.nonEmpty) {
            contentTable.filter(item =>
              playableIds.contains(item("id")) && item("group") == "playable"
            )
          } else {
            contentTable.filter(item =>
              item("title").contains(title) && item("group") == "playable"
            )
          }

          complete(AlexaResponse(playables))
        }
      }
    }
  }

  val allRoutes: Route = concat(alexaRequest)
}
