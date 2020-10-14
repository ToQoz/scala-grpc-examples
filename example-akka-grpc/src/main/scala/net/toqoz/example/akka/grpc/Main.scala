package net.toqoz.example.akka.grpc

object Main {
  private val port = Option(System.getenv("SERVER_PORT")).getOrElse("3012").toInt

  def main(args: Array[String]): Unit = Server(port = port).start()
}
