package com.datastax.driver.spark.rdd.partitioner.dht

import java.net.InetAddress

case class TokenRange[V, T <: Token[V]] (
    start: T, end: T, endpoints: Set[InetAddress], rowCount: Option[Long]) {

  def isWrapAround: Boolean =
    start >= end

  def unwrap(implicit tokenFactory: TokenFactory[V, T]): Seq[TokenRange[V, T]] = {
    val minToken = tokenFactory.minToken
    if (isWrapAround)
      Seq(
        TokenRange(start, minToken, endpoints, rowCount.map(_ / 2)),
        TokenRange(minToken, end, endpoints, rowCount.map(_ / 2)))
    else
      Seq(this)
  }
}