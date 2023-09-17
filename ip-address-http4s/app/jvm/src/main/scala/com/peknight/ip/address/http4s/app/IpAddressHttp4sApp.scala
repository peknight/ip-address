package com.peknight.ip.address.http4s.app

import cats.effect.{IO, IOApp}

object IpAddressHttp4sApp extends IOApp.Simple:

  val run: IO[Unit] =
    for
      _ <- IO.unit
    yield ()

end IpAddressHttp4sApp
