package fix

import scala.collection.mutable
import scalafix.v0._
import scala.meta._

object Scalafixupgrade_v0_6 extends Rule("Scalafix_v0_6") {

  override def fix(ctx: RuleCtx): Patch = {
    val imports = mutable.Map.empty[String, Importer]
    var patch = Patch.empty
    def addImporter(importer: Importer, importees: List[Importee]): Unit = {
      imports(importer.syntax) = importer
      importees.foreach { i =>
        patch += ctx.removeImportee(i)
      }
    }

    ctx.tree.traverse {
      case q""" "ch.epfl.scala" % "sbt-scalafix" % ${v: Lit.String} """ =>
        patch += ctx.replaceTree(
          v,
          Lit.String("0.6.0-M7").syntax
        )
      case t @ q"scalafixSourceroot := sourceDirectory.in(Compile).value" =>
        patch += ctx.replaceTree(
          t,
          "scalacOptions += s\"-P:semanticdb:sourceroot:${sourceDirectory.in(Compile).value}\""
        )
      case t @ q"scalaVersion in ThisBuild := V.scala212" =>
        patch += ctx.replaceTree(
          t,
          """
            |inThisBuild(
            |  List(
            |    scalaVersion := V.scala212,
            |    addCompilerPlugin(scalafixSemanticdb),
            |    scalacOptions += "-Yrangepos"
            |  )
            |)""".stripMargin
        )
      case Importer(ref, importees) =>
        val syntax = ref.syntax
        if (syntax.startsWith("scala.meta.contrib") ||
            syntax.startsWith("scalafix.syntax")) {
          // do nothing
        } else if (syntax.startsWith("org.langmeta.") ||
                   syntax.startsWith("scala.meta.")) {
          addImporter(importer"scala.meta._", importees)
        } else if (syntax.startsWith("scalafix.")) {
          addImporter(importer"scalafix.v0._", importees)
        }
      case t @ init"SemanticRuleSuite(..$_)" =>
        patch += ctx.replaceTree(
          t,
          """scalafix.testkit.SemanticRuleSuite(
            |      BuildInfo.inputClassdirectory,
            |      BuildInfo.inputSourceroot,
            |      Seq(BuildInfo.outputSourceroot)
            |    )""".stripMargin
        )
    }

    patch ++ imports.values.map(ctx.addGlobalImport)
  }

}
