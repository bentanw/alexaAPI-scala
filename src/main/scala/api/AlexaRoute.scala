package api

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.ContentTypes

trait AlexaRouteDefinitions {
  def alexaRequest: Route
}

object AlexaRoute extends AlexaRouteDefinitions {
  def alexaRequest: Route = get {
    path("api"/"alexa") {
      complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "Hello World from scala akka http server!"))
    }
  }
}
