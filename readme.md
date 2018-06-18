# Giter8 template to create Scalafix rules

Assuming you are located inside the top directory of an existing `myproject` directory that is available on
GitHub through `$USERNAME/myproject`:

```
sbt new scalacenter/scalafix.g8 --rule=myproject --version=v1
cd myproject/scalafix
sbt tests/test
```
