// See LICENSE for license details.

package test

import Chisel._
import memlib._

class WriteThenReader extends Module {
  val io = new Bundle {
    val dataToWrite  = UInt(INPUT, 32)
    val dataRead  = UInt(OUTPUT, 32)
    val address  = UInt(INPUT, 5)
    val writing  = Bool(INPUT)
  }
  val mem = MemoryLibrary.create(2, UInt(width = 32))
  when (io.writing) {
    mem.write(io.address, io.dataToWrite)
  }.otherwise {
    io.dataRead := mem.read(io.address)
  }
}


//class DecoupledGCD extends Module {
//  val io = new Bundle {
//    val in = Decoupled(new Bundle {
//      val a = UInt(width = 32)
//      val b = UInt(width = 32)
//    }).flip
//    val out = Decoupled(UInt(width = 32))
//  }
//
//  // Control State
//  val busy = Reg(init = Bool(false))
//  val done = Reg(init = Bool(false))
//  // Data State
//  val mem = MemoryLibrary.create(2, UInt(width = 32))
//  val x = mem.read(UInt(0))
//  val y = mem.read(UInt(1))
//
//  // Control Logic
//  io.in.ready := !busy
//  io.out.valid := done
//  when (busy & y === UInt(0)) {
//    done := Bool(true)
//  }
//  when (done && io.out.ready) {
//    busy := Bool(false)
//  }
//  val start = io.in.valid && io.in.ready
//  when (start) {
//    busy := Bool(true)
//    done := Bool(false)
//  }
//
//  // Data Logic
//  when (x > y)   { mem.write(UInt(0), x -% y)}
//  .otherwise     { mem.write(UInt(1), y -% x)}
//  when (start) { w := io.in.bits.a; y := io.in.bits.b }
//  io.out.bits := x
//
//}

