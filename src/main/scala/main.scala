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

@main def httpserver: Unit =

  implicit val actorSystem: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "akka-http")
  implicit val executionContext: scala.concurrent.ExecutionContext = actorSystem.executionContext
  
  val route1 = get(
    path("hello") {
      complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "Hello world from scala akka http server!"))
    }
  )

  val route2 = get(
    path("new-hello-world") {
      complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "Hello world2222 from scala akka http server!"))
    }
  )

  val route = cors(){
    concat(route1,route2)
  }

  val serverBinding = Http().newServerAt("127.0.0.1", 8080).bind(route)
  
  serverBinding.onComplete {
    case Success(binding) =>
      val address = binding.localAddress
      println(s"Server online at http://${address.getHostString}:${address.getPort}/")
    case Failure(exception) =>
      println(s"Failed to bind to 127.0.0.1:8080!")
      exception.printStackTrace()
      actorSystem.terminate()
  }

  Await.result(actorSystem.whenTerminated, Duration.Inf)