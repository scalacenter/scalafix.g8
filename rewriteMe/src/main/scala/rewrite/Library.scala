package rewrite

class Foo {
  def filter(cond: Int => Boolean) = this
  def headOption = this
}
object Library {
  val lst = Seq(1, 2, 2)
  val foo = new Foo
  foo.filter(_ > 2).headOption

  lst.filter(_ > 2).headOption

  Set(3, 4).filter(_ > 2).headOption

  Map(1 -> 4).filter(_._1 > 2).headOption

  def main(args: Array[String]) {
    lst.foreach(println)
  }
}
