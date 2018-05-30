package fix

import scalafix._
import scala.meta._

final case class V06_v0_6(index: SemanticdbIndex)
    extends SemanticRule(index, "V06_v0_6") {

  override def fix(ctx: RuleCtx): Patch = {
    ctx.debugIndex()
    println(s"Tree.syntax: " + ctx.tree.syntax)
    println(s"Tree.structure: " + ctx.tree.structure)
    Patch.empty
  }

}
