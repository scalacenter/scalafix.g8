package rewrite
object Library {
  val lst = Seq(1, 2, 2)
  lst.filter(_ > 2 /* this is a comment */)
      /* not joking */ . /* pathological */ headOption
  def main(args: Array[String]) {
    lst.foreach(println)
  }
}
