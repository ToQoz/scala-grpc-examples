package net.toqoz.example.core.service.greet_service

import scala.concurrent.Future

class GreetService extends GreetServiceGrpc.GreetService {
  override def sayHello(req: HelloRequest): Future[HelloReply] = {
    val reply = HelloReply(message = "Hello " + req.name)
    Future.successful(reply)
  }
}
