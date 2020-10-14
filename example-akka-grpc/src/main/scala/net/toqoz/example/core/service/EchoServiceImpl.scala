package net.toqoz.example.core.service

import akka.stream.Materializer

import scala.concurrent.Future

class EchoServiceImpl(implicit mat: Materializer) extends EchoService {
  import mat.executionContext

  override def echo(req: EchoRequest): Future[EchoReply] = {
    val reply = EchoReply(text = req.text)
    Future.successful(reply)
  }
}
