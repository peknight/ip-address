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
        val forwardedAddr = req.headers.get[`X-Forwarded-For`].flatMap(_.values.find(_.isDefined).flatten)
        val remoteAddr = req.remoteAddr
        val ipv4Addr = forwardedAddr.flatMap(_.asIpv4).orElse(remoteAddr.flatMap(_.asIpv4))
        val ipv6Addr = forwardedAddr.flatMap(_.asIpv6).orElse(remoteAddr.flatMap(_.asIpv6))
        Ok(IpAddress(ipv4Addr.map(_.toString), ipv6Addr.map(_.toString)).asJson.dropNullValues)
    }
end IpAddressRoutes
