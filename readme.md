At the root of your scalafix.g8 generated build, run this migration

```
coursier launch ch.epfl.scala:scalafix-cli_2.12.4:0.6.0-M6 -- -r github:olafurpg/scalafix-upgrade/v0.6 --verbose
```

The rewrite is syntactic and runs on both `*.scala` and `*.sbt` files.
The rule may not work as expected if you have customized the auto-generated
build.sbt and test suite.
