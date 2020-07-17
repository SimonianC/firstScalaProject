package pers

case class Pers( id : Int,  nom : String ,  age : Int) {
  override def toString: String = id.toString + "," + nom + ","+ age.toString

}
