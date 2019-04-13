package service

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

object Boot extends App {
  val log = LoggerFactory.getLogger("Boot")

  // needed to run the route
  implicit val system = ActorSystem()

  implicit val materializer = ActorMaterializer()
  // needed for the future map/flatmap in the end and future in fetchItem and saveOrder
  implicit val executionContext = system.dispatcher

  implicit val timeout = Timeout(30.seconds)

  val route =
    path("healthcheck") {
      pathEndOrSingleSlash {
        get {
          complete {
            log.info("Received healthcheck, replying with OK")
            "OK"
          }
        }
      }
    }

  val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 8080)
  log.info("Listening on port 8080...")
}
