package net.toqoz.example.core.service.echo_service

import scala.concurrent.Future

class EchoService extends EchoServiceGrpc.EchoService {
  override def echo(req: EchoRequest): Future[EchoReply] = {
    val reply = EchoReply(text = req.text)
    Future.successful(reply)
  }
}
