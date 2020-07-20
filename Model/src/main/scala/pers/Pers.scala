package pers

import java.util.UUID

case class Pers( id : Int,  nom : String ,  age : Int) {

  override def toString: String = id.toString + "," + nom + "," + age.toString

}
object  Pers{
  def apply(str : String) : Pers = {
    val tmp = str.split(",")
    Pers(tmp(0).toInt, tmp(1),tmp(2).toInt)
  }
  def apply(id : String,  nom : String ,  age : String) : Pers = {

    Pers(id.toInt, nom,age.toInt)
  }
}
object Unmarshaller {

  def unmarshallValeursFoncieres(pers: List[String]): Pers = {
    def liftStringWithEmpty(index: Int, pers: List[String]): String = pers.lift(index).getOrElse("")
    Pers(
      liftStringWithEmpty(0, pers),
      liftStringWithEmpty(1, pers),
      liftStringWithEmpty(2, pers))

  }
}