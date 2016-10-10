// See LICENSE for license details.

package test

import Chisel._
import Chisel.testers.BasicTester

import scala.util.Random

class MemoryLibraryTester extends BasicTester {
  val dut = Module(new WriteThenReader)
  val datas = Seq(11, 50, 28, 15)
  val count = Reg(init = UInt(datas.size - 1, width = log2Up(datas.size)))
  val values = Vec(datas.map(x => UInt(x)))

  val writing = Reg(init = Bool(true))
  dut.io.writing := writing
  dut.io.address := count
  dut.io.dataToWrite := values(count)

  assert( dut.io.dataRead === values(count) )
  when( count === UInt(0) && writing === Bool(false)) {
    stop()
  } .otherwise {
    when( count === UInt(0) ) {
      count := UInt(datas.size - 1)
      writing := !writing
    }.otherwise {
      count := count - UInt(1)
    }
  }
 

}

