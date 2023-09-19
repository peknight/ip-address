package com.peknight.ip.address.http4s.service.routes

import cats.Monad
import com.peknight.ip.address.IpAddress
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.circe.*
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`X-Forwarded-For`
import org.http4s.{HttpRoutes, Method}

object IpAddressRoutes:
  def routes[F[_] : Monad]: HttpRoutes[F] =
    object dsl extends Http4sDsl[F]
    import dsl.*
    given CanEqual[Path, Path] = CanEqual.derived
    given CanEqual[Method, Method] = CanEqual.derived
    HttpRoutes.of[F] {
      case req @ GET -> Root / "ip" =>
        val addr = req.headers.get[`X-Forwarded-For`].flatMap(_.values.find(_.isDefined).flatten).orElse(req.remoteAddr)
        Ok(IpAddress(addr.map(_.toString)).asJson.dropNullValues)
    }
end IpAddressRoutes
