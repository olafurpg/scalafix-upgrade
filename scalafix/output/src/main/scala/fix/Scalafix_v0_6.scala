package fix

import java.io.File
import scala.meta.contrib._
import scala.meta._
import scalafix.syntax._
import scala.collection.mutable.ListBuffer
import scalafix.v0._

object Scalafix_v0_6_Test
    extends scalafix.testkit.SemanticRuleSuite {
  ListBuffer.empty[Int].append(1)
  Lit.String("1").collectFirst { case q"1" => q"2".parents; Patch.empty }
  // Add code that needs fixing here.
}

object BuildInfo {
  def inputClassdirectory: File = null
  def inputSourceroot: File = null
  def outputSourceroot: File = null
}
