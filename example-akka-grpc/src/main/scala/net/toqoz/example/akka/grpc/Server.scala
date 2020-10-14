package net.toqoz.example.akka.grpc

import java.util.logging.Logger

import akka.actor.ActorSystem
import akka.grpc.scaladsl.{ ServerReflection, ServiceHandler }
import akka.http.scaladsl.model.{ HttpRequest, HttpResponse }
import akka.http.scaladsl.{ Http, HttpConnectionContext }
import akka.stream.{ ActorMaterializer, Materializer }
import com.typesafe.config.ConfigFactory
import net.toqoz.example.core.service._

import scala.concurrent.{ ExecutionContext, Future }

case class Server(port: Int) {
  private val logger = Logger.getLogger(classOf[Server].getName)

  def start(): Unit = {
    val conf = ConfigFactory
      .parseString("akka.http.server.preview.enable-http2 = on")
      .withFallback(ConfigFactory.defaultApplication())
    val system = ActorSystem("Server", conf)

    implicit val sys: ActorSystem = system
    implicit val mat: Materializer = ActorMaterializer()
    implicit val ec: ExecutionContext = sys.dispatcher

    val greeterService: PartialFunction[HttpRequest, Future[HttpResponse]] =
      GreetServiceHandler.partial(new GreetServiceImpl())

    val echoService: PartialFunction[HttpRequest, Future[HttpResponse]] =
      EchoServiceHandler.partial(new EchoServiceImpl)
    val reflectionService = ServerReflection.partial(List(GreetService, EchoService))

    val serviceHandlers: HttpRequest => Future[HttpResponse] =
      ServiceHandler.concatOrNotFound(greeterService, echoService, reflectionService)

    val binding = Http().bindAndHandleAsync(
      serviceHandlers,
      interface = "127.0.0.1",
      port = port,
      connectionContext = HttpConnectionContext()
    )

    logger.info("Server starting on " + port)
    binding.foreach { binding =>
      logger.info("Server started, listening on " + binding.localAddress)
    }

    binding.onComplete { _ =>
      system.terminate()
    }

    system.registerOnTermination {
      logger.info("Shutting down server...")
    }
  }
}
