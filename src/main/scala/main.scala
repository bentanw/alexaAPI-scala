import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import scala.util.{Failure, Success}
import scala.concurrent.duration.Duration
import scala.concurrent.Await

import api.AlexaRoute

object HttpServer extends App {
  implicit val actorSystem: ActorSystem[Nothing] =
    ActorSystem(Behaviors.empty, "akka-http")
  implicit val executionContext: scala.concurrent.ExecutionContext =
    actorSystem.executionContext

  val route = cors() {
    concat(AlexaRoute.allRoutes)
  }

  // start up server
  Http().newServerAt("127.0.0.1", 8080).bind(route).onComplete {
    case Success(binding) =>
      val address = binding.localAddress
      println(
        s"Server online at http://${address.getHostString}:${address.getPort}/"
      )
    case Failure(exception) =>
      println(s"Failed to bind")
      exception.printStackTrace()
      actorSystem.terminate()
  }
  Await.result(actorSystem.whenTerminated, Duration.Inf)
}
