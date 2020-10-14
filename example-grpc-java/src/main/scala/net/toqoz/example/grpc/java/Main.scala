package net.toqoz.example.grpc.java

object Main {
  private val port = Option(System.getenv("SERVER_PORT")).getOrElse("3010").toInt

  def main(args: Array[String]): Unit = Server(port = port).start()
}
