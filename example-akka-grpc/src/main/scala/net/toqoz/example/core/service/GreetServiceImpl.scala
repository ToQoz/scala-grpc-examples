package net.toqoz.example.core.service

import akka.stream.Materializer

import scala.concurrent.Future

class GreetServiceImpl(implicit mat: Materializer) extends GreetService {
  import mat.executionContext

  override def sayHello(req: HelloRequest): Future[HelloReply] = {
    val reply = HelloReply(message = "Hello " + req.name)
    Future.successful(reply)
  }
}
