[![Build status](https://github.com/scalacenter/scalafix.g8/workflows/CI/badge.svg)](https://github.com/scalacenter/scalafix.g8/actions?query=workflow)

# Giter8 template to create Scalafix rules

Assuming you are located inside the directory `myproject`:

```
sbt new scalacenter/scalafix.g8 --rule=myproject --version=v1
cd scalafix
sbt tests/test
```

If the repository is available on GitHub through at `$USERNAME/myproject`
then users can run the rule with `scalafix --rules github:$USERNAME/myproject/v1`.

The `v1` part is the name of you rule and can be any string.
For example, if the rule contains migration rules for version
8 of your library it's good to call it `v8`.
If the rule is a linter that prohibits vars, the name can be `NoVars`.


## Scalafix v0.6 migration

To try out the latest Scalafix milestone in the v0.6.x series,
follow the instructions from the readme in the [scalafix/](scalafix/readme.md)
directory.

## Template license

Written in 2020 by Scala Center

To the extent possible under law, the author(s) have dedicated all copyright and related
and neighboring rights to this template to the public domain worldwide.
This template is distributed without any warranty. See <https://creativecommons.org/publicdomain/zero/1.0/>.
