package net.petitviolet.metas.app

import net.petitviolet.metas.app.macros._


object PointApp extends App {
  val p1 = AvailablePoint(100)
  val p2 = AvailablePoint(200)

  assert(Point.add(p1, p2) == p1 + p2)


  val r1 = ReceivedPoint(200)
  val r2 = ReceivedPoint(300)

  assert(Point.add(r1, r2) == r1 + r2)

  val o1 = OtherPoint(50)
  val o2 = OtherPoint(30)
  // cannot compile
  // Point.add(o1, o2)
}

