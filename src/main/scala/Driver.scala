// See LICENSE for license details.

package test

import Chisel._
import firrtl._
import java.io._


object Driver extends App {
  val rootDir = new File(".").getCanonicalPath()
  val dut = "MemoryLibraryTester"
  val buildDir = s"${rootDir}/build"
  val verilogFile = s"${buildDir}/${dut}.v"
  val cppHarness = s"${rootDir}/chisel3/src/main/resources/top.cpp"

  // Run Chisel 3 
  val s = Chisel.Driver.emit(() => new MemoryLibraryTester)
  println(s)

  // Parse circuit into FIRRTL
  val circuit = firrtl.Parser.parse(s.split("\n"))

  val writer = new PrintWriter(new File(verilogFile))
  val compiler = new firrtl.VerilogCompiler
  // Compile to verilog
  compiler.compile(circuit, Annotations.AnnotationMap(Seq.empty), writer)
  writer.close()

  // Build executable
  println("Running Chisel.Driver.verilogToCpp")
  Chisel.Driver.verilogToCpp(dut, dut, new File(buildDir), Seq(), new File(cppHarness)).!
  Chisel.Driver.cppToExe(dut, new File(buildDir)).!
  if (Chisel.Driver.executeExpectingSuccess(dut, new File(buildDir))) {
    println("Test Executed Successfully!")
  } else {
    println("ERROR: Test Failed")
  }
}
