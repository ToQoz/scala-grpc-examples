package net.toqoz.example.grpc.java

import java.util.logging.Logger

import io.grpc.ServerBuilder
import io.grpc.protobuf.services.ProtoReflectionService
import net.toqoz.example.core.service.echo_service._
import net.toqoz.example.core.service.greet_service._

import scala.concurrent.ExecutionContext

case class Server(port: Int) {
  private val executionContext = ExecutionContext.global
  private val logger = Logger.getLogger(classOf[Server].getName)

  def start(): Unit = {
    logger.info("Server starting on " + port)

    val serv = ServerBuilder
      .forPort(port)
      .addService(GreetServiceGrpc.bindService(new GreetService, executionContext))
      .asInstanceOf[ServerBuilder[_]]
      .addService(EchoServiceGrpc.bindService(new EchoService, executionContext))
      .asInstanceOf[ServerBuilder[_]]
      .addService(ProtoReflectionService.newInstance())
      .asInstanceOf[ServerBuilder[_]]
      .build()
      .start()

    logger.info("Server started, listening on " + port)
    sys.addShutdownHook {
      logger.info("Shutting down server...")
      serv.shutdown()
    }
    serv.awaitTermination()
  }
}
